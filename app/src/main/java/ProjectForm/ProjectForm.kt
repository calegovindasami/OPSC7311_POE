package ProjectForm

import Data.ProjectViewModel
import Data.TaskViewModel
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.util.Pair
import com.example.opsc7311_poe.R
import com.google.android.gms.tasks.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.slider.RangeSlider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.util.Calendar
import java.util.Date
import kotlin.math.min

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectForm.newInstance] factory method to
 * create an instance of this fragment.
 */

private var startDate: Date = Date()
private var endDate: Date = Date()



class ProjectForm : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_project_form, container, false)

        val btnDateRange = view.findViewById<Button>(R.id.btnProjectDateRange)

        btnDateRange.setOnClickListener() {
            createDatePicker(btnDateRange)
        }

        val btnSubmit = view.findViewById<Button>(R.id.btnProjectSubmit)

        btnSubmit.setOnClickListener {
            addProject(getFormData(view))
        }

        return view
    }

    private fun getFormData(view: View): ProjectViewModel {
        val projectName = view.findViewById<EditText>(R.id.edtProjectNameInput).text.toString()
        val description = view.findViewById<EditText>(R.id.edtProjectDescriptionInput).text.toString()
        val timeRange = view.findViewById<RangeSlider>(R.id.rangeProjectDailyHours)
        val minHours = timeRange.valueFrom.toInt()
        val maxHours = timeRange.valueTo.toInt()
        return ProjectViewModel(projectName, description, startDate, endDate, minHours, maxHours, null)
    }

    private fun addProject(project: ProjectViewModel) {
        val db = Firebase.firestore
        db.collection("users").document("st10083941").collection("projects").add(project)
            .addOnSuccessListener {
                Toast.makeText(activity?.let{it}, "${project.name} has been added!", Toast.LENGTH_LONG)
            }
            .addOnFailureListener {
                Toast.makeText(activity?.let{it}, "${it.toString()}", Toast.LENGTH_LONG)
            }
    }

    private fun getProject() {
        val db = Firebase.firestore
        val docRef =  db.collection("users").document("st10083941").collection("projects").document("jV6Qj5X4Yat2lgHwYm2W")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val project = documentSnapshot.toObject<ProjectViewModel>()
            val name = project!!.name
        }
    }

    private fun getProjects(userId: String): MutableList<ProjectViewModel> {
        val db = Firebase.firestore
        var projectList:MutableList<ProjectViewModel> = mutableListOf()
        val docRef =  db.collection("users").document(userId).collection("projects")
        docRef.get().addOnSuccessListener {
            projects ->
            for (proj in projects) {
                var project = proj.toObject<ProjectViewModel>()
                projectList.add(project)
            }
        }
            .addOnFailureListener() {
                //TODO
            }

        return projectList
    }



    private fun createDatePicker(button: Button){
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates").setSelection(Pair(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
                .build()

        dateRangePicker.addOnPositiveButtonClickListener {
            val dates = dateRangePicker.selection
            startDate = Date(dates!!.first)
            endDate = Date(dates!!.second)

        }

        dateRangePicker.show(parentFragmentManager, "Tag")
    }

    fun groupByHours() {
        val list = listOf(ProjectViewModel())

        list.forEach {
            p ->
            var total = 0
            p.tasks!!.forEach {
                task ->
                total += task.numberOfHours

            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProjectForm.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProjectForm().apply {
                arguments = Bundle().apply {

                }
            }
    }
}