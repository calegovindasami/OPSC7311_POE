package com.example.opsc7311_poe

import ProjectForm.ProjectForm
import TaskForm.TaskForm
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val projectForm = ProjectForm.newInstance()
        val taskForm = TaskForm.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.main_container, taskForm).commit()

    }
}