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

class MainActivity : AppCompatActivity() {
    // TODO: Add firebase
    private lateinit var dbref : DatabaseReference
    private lateinit var projectRecyclerView: RecyclerView
    private lateinit var projectArrayList: ArrayList<ProjectViewModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //projectRecyclerView = findViewById(R.id.projectRecyclerView)
        projectRecyclerView.layoutManager = LinearLayoutManager(this)
        projectRecyclerView.setHasFixedSize(true)

        projectArrayList = arrayListOf<ProjectViewModel>()
        getProjectData()
    }
    // https://www.youtube.com/watch?v=VVXKVFyYQdQ ref for how I did this code
    private fun getProjectData() {
        dbref = FirebaseDatabase.getInstance().getReference("Project")
        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (projectSnapshot in snapshot.children) {
                        val project = projectSnapshot.getValue(ProjectViewModel::class.java)
                        projectArrayList.add(project!!)
                    }
                    projectRecyclerView.adapter = ProjectAdapter(projectArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}