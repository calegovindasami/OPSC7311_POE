package com.example.opsc7311_poe

//import Services.ExportService
import ProjectForm.ProjectForm
import Services.ExportService
import Services.HoursService
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.util.Pair
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import data.GraphViewModel
import data.ProjectViewModel
import data.TaskViewModel
import data.graphData
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var db: FirebaseFirestore = Firebase.firestore
    private lateinit var barChart: BarChart
    private lateinit var projectList: MutableList<ProjectViewModel>

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val btnExcel = view.findViewById<CardView>(R.id.btnExport)
        val btnViewProjects = view.findViewById<CardView>(R.id.btnViewProjects)
        val btnFilterGraph = view.findViewById<Button>(R.id.btnGraphFilter)
        val txtGoals = view.findViewById<TextView>(R.id.txtUseGoals)

        auth = Firebase.auth
        val uid = auth.uid!!

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        val previousMonth = LocalDate.now().minusMonths(1)
        val numberOfDays = previousMonth.lengthOfMonth()
        val year = previousMonth.year
        val month = previousMonth.monthValue

        val start = dateFormat.parse("01-${month}-$year")
        val end = dateFormat.parse("$numberOfDays-${month}-$year")

        projectList = mutableListOf()

        val docRef =  db.collection("users").document(uid).collection("projects")
        docRef.get().addOnCompleteListener() {
            if (it.isSuccessful) {
                var projects = it.result.documents
                for (p in projects) {
                    var project = p.toObject<ProjectViewModel>()
                    projectList.add(project!!)
                }
                val service = HoursService()
                val tasks = service.getTasks(projectList)
                barChart=view.findViewById(R.id.home_bar_chart)
                displayBarChart(service, tasks, start, end)

                btnFilterGraph.setOnClickListener(){
                    createDatePicker(txtGoals)
                }
            }
        }

        btnViewProjects.setOnClickListener {
            if (projectList.size == 0) {
                Snackbar.make(requireView(), "You have no projects.", Snackbar.LENGTH_LONG).show()
            }
            else {
                val viewProject = ViewProject()
                requireActivity().supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    replace(R.id.flNavigation, viewProject)
                    addToBackStack(null)
                }
            }
        }

        val btnAddProject = view.findViewById<CardView>(R.id.btnHomeAddProject)
        btnAddProject.setOnClickListener {
            val projectForm = ProjectForm()
            requireActivity().supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                replace(R.id.flNavigation, projectForm)
                addToBackStack(null)
            }
        }

        val btnLogout = view.findViewById<CardView>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }

        val getTasks = HoursService()
        val tasks = getTasks.getTasks(projectList)

        btnExcel.setOnClickListener(){
           val service = ExportService(requireContext())

            service.createFile(tasks)
        }

        return view
    }

    private fun filterProjects(start: Date, end: Date, projectList: MutableList<ProjectViewModel>): MutableList<ProjectViewModel> {
        val filteredList: MutableList<ProjectViewModel> = mutableListOf()

        for (proj in projectList) {
            val isTrue = proj.startDate!! >= start && proj.endDate!! <= end
            if (proj.startDate!! >= start && proj.endDate!! <= end) {
                filteredList.add(proj)
            }
        }

        return filteredList

    }

    private fun displayBarChart(service: HoursService, tasks: MutableList<TaskViewModel>, start: Date, end: Date) {
        var gData = service.getGraphData(tasks, start, end)
        val graphViewModel = GraphViewModel()

        var i = 1f
        gData.forEach() { g ->
            graphViewModel.list.add(BarEntry(i, g.toFloat()))
            i++
        }

        val barDataSet = BarDataSet(graphViewModel.list, "List")

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)

        barChart.setFitBars(true)

        barChart.data = barData

        barChart.description.text = "Bar Chart"

        barChart.animateY(2000)
    }

    private fun createDatePicker(textView:TextView){
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates").setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds())
                )
                .build()

        dateRangePicker.addOnPositiveButtonClickListener {
            var firstDate = it.first
            var secondDate = it.second

            var startDate = Date(firstDate)
            var endDate = Date(secondDate)

            var filteredProjects = filterProjects(startDate,endDate,projectList)
            val service = HoursService()

            val tasks = service.getTasks(filteredProjects)

            displayBarChart(service,tasks,startDate,endDate)

            var diff = endDate.time - startDate.time

            val days = (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)).toInt()

            val average = service.getAverageHours(filteredProjects,days)
            val userHours = service.checkUserHours(tasks)

            var message:String
            if (userHours < average)
            {
                message = "You are below the monthly required hours of $average/Day"
            }
            else if (userHours > average)
            {
                message = "You are above the monthly required hours of $average/Day"
            }
            else
            {
                message = "You are meeting the monthly required hours of $average/Day"
            }

            textView.text = message


        }

        dateRangePicker.show(parentFragmentManager, "Tag")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}