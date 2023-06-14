package com.example.opsc7311_poe

import TaskForm.TaskForm
import data.TaskViewAdapter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import data.ProjectViewModel
import data.TaskViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PROJECTID = "projectId"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewTask.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewTask : Fragment() {
    // TODO: Rename and change types of parameters
    private var projectId: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private lateinit var tasks: MutableList<TaskViewModel>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getString(ARG_PROJECTID)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_task, container, false)

        auth = Firebase.auth

        val uid = auth.uid!!

        //Gets tasks from selected user project
        val docRef =  db.collection("users").document(uid).collection("projects").document(projectId!!)
        docRef.get().addOnCompleteListener() {
            if (it.isSuccessful) {
                var docSnap = it.result
                val project = docSnap.toObject<ProjectViewModel>()!!
                tasks = project.tasks!!
                if (tasks.size > 0) {
                    recyclerView = view.findViewById<RecyclerView>(R.id.task_view)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)
                    val adapter = TaskViewAdapter(tasks)
                    recyclerView.adapter = adapter
                }
            }
            else {
                //TODO
            }
        }

        val btnAddTask = view.findViewById<FloatingActionButton>(R.id.btnAddTask)
        //Adds a task for specified project
        btnAddTask.setOnClickListener() {
            val taskForm = TaskForm.newInstance(projectId!!)
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, taskForm).commit()

        }

        val btnTaskBack = view.findViewById<ImageButton>(R.id.btnTaskViewBack)

        btnTaskBack.setOnClickListener() {
            val projectView = ViewProject.newInstance()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.auth_view, projectView).commit()
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
        fun newInstance(projectId: String) =
            ViewTask().apply {
                arguments = Bundle().apply {
                    putString(ARG_PROJECTID, projectId)
                }
            }
    }
}