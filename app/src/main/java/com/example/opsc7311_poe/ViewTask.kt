package com.example.opsc7311_poe

import Data.TaskViewAdapter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.ProjectViewAdapter
import data.TaskViewModel
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TASK = "task"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewTask.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewTask : Fragment() {
    // TODO: Rename and change types of parameters
    private var tasks: MutableList<TaskViewModel>? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tasks = it.getSerializable(ARG_TASK) as MutableList<TaskViewModel>
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_task, container, false)

        if (tasks != null) {
            recyclerView = view.findViewById(R.id.task_view)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = TaskViewAdapter(tasks!!)
            recyclerView.adapter = adapter
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewTask.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(tasks: MutableList<TaskViewModel>) =
            ViewTask().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_TASK, tasks as Serializable)
                }
            }
    }
}