package com.example.opsc7311_poe

import ProjectForm.ProjectForm
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val projectForm = ProjectForm.newInstance()

        supportFragmentManager.beginTransaction().replace(R.id.main_container, projectForm).commit()

    }
}