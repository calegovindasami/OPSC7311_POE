package com.example.opsc7311_poe

import Services.HoursService
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GraphView.newInstance] factory method to
 * create an instance of this fragment.
 */
class GraphView : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth

    private var db: FirebaseFirestore = Firebase.firestore

    lateinit var barChart:BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_graph_view, container, false)

        val sharedViewModel = ViewModelProvider(requireActivity()).get(graphData::class.java)
        val projectList: MutableList<ProjectViewModel> = sharedViewModel.projects




        val args = arguments
       // val tasks: MutableList<TaskViewModel>? = args?.getSerializable("tasks") as? MutableList<TaskViewModel>
        val weeks = args?.getInt("Weeks")



        val service = HoursService()
        val tasks = service.getTasks(projectList)

        var graphData :IntArray = IntArray(7)
        if (tasks != null) {
          //  graphData= weeks?.let { service.calcBarAverage(tasks, it) }!!
            graphData = service.calcBarAverage(tasks,4)
        }
        barChart=view.findViewById(R.id.bar_chart)


        val graphViewModel = GraphViewModel()



        graphViewModel.list.add(BarEntry(1f,graphData[0].toFloat()))
        graphViewModel.list.add(BarEntry(2f,graphData[1].toFloat()))
        graphViewModel.list.add(BarEntry(3f,graphData[2].toFloat()))
        graphViewModel.list.add(BarEntry(4f,graphData[3].toFloat()))
        graphViewModel.list.add(BarEntry(5f,graphData[4].toFloat()))
        graphViewModel.list.add(BarEntry(6f,graphData[5].toFloat()))
        graphViewModel.list.add(BarEntry(7f,graphData[6].toFloat()))


        val barDataSet= BarDataSet(graphViewModel.list,"List")

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        barDataSet.valueTextColor= Color.BLACK

        val barData= BarData(barDataSet)

        barChart.setFitBars(true)

        barChart.data= barData

        barChart.description.text= "Bar Chart"

        barChart.animateY(2000)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GraphView.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                GraphView().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)

                    }
                }
    }
}