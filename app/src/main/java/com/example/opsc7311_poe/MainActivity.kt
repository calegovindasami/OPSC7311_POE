package com.example.opsc7311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import data.ProjectAdapter
import data.ProjectViewModel
import data.TaskAdapter
import data.TaskViewModel

class MainActivity : AppCompatActivity() {
    // TODO: Add firebase
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
        projectRecyclerView.adapter = TaskAdapter(getTasks())

        // Method used to get data from task
        taskRecyclerView = findViewById(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.setHasFixedSize(true)

        taskArrayList = arrayListOf<TaskViewModel>()
        taskRecyclerView.adapter = TaskAdapter(getProjects())

    }

    private fun getTaskData() {
        dbref = FirebaseDatabase.getInstance().getReference("Task")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (taskSnapshot in snapshot.children) {
                        val task = taskSnapshot.getValue(TaskViewModel::class.java)
                        taskArrayList.add(task!!)
                    }
                    taskRecyclerView.adapter = TaskAdapter(taskArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


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