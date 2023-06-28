package com.example.opsc7311_poe

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import data.GraphViewModel

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

        barChart=view.findViewById(R.id.bar_chart)


        val graphViewModel = GraphViewModel()

        graphViewModel.list.add(BarEntry(1f,100f))
        graphViewModel.list.add(BarEntry(2f,200f))
        graphViewModel.list.add(BarEntry(3f,300f))
        graphViewModel.list.add(BarEntry(4f,400f))
        /*graphViewModel.list.add(BarEntry(5f,500f))
        graphViewModel.list.add(BarEntry(6f,500f))
        graphViewModel.list.add(BarEntry(7f,500f))*/


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