package com.example.opsc7311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import data.ProjectAdapter
import data.ProjectViewModel
import data.TaskAdapter
import data.TaskViewModel

class MainActivity : AppCompatActivity() {
    // TODO: Add the UI to make sure the code works, The Var names are set so you can use those or just change them,
    //  Apologizes as I tried to do the UI but Android Studio Freezes the second I touch the code. When my power comes
    //  on I'll hop onto discord to complete this but with everything else combined we should be good to go
    private var db: FirebaseFirestore = Firebase.firestore
    // for projects
    private lateinit var projectRecyclerView: RecyclerView
    private lateinit var projectArrayList: ArrayList<ProjectViewModel>
    // for tasks
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskArrayList: ArrayList<TaskViewModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Method used to get data from project
        projectRecyclerView = findViewById(R.id.projectRecyclerView)
        projectRecyclerView.layoutManager = LinearLayoutManager(this)
        projectRecyclerView.setHasFixedSize(true)

        projectArrayList = arrayListOf<ProjectViewModel>()
        projectRecyclerView.adapter = ProjectAdapter(getProjects("UserID") as ArrayList<ProjectViewModel>)

        // Method used to get data from task
        taskRecyclerView = findViewById(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.setHasFixedSize(true)

        taskArrayList = arrayListOf<TaskViewModel>()
        taskRecyclerView.adapter = TaskAdapter(getTasks("UserID"))

    }
    // No idea on why the error is on tasks
    private fun getTasks(userId: String): MutableList<TaskViewModel> {
        var taskList:MutableList<TaskViewModel> = mutableListOf()
        val docRef =  db.collection("users").document(userId).collection("task")
        docRef.get().addOnSuccessListener {
                tasks ->
            for (task in tasks) {
                var newTask = task.toObject<TaskViewModel>()
                taskList.add(newTask)
            }
        }
            .addOnFailureListener() {
                //TODO
            }

        return taskList
    }
        private fun getProjects(userId: String): MutableList<ProjectViewModel> {
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

}