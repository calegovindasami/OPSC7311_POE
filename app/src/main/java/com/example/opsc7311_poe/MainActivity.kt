package com.example.opsc7311_poe

import Authentication.LoginFragment
import Authentication.RegisterFragment
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
        val register = RegisterFragment.newInstance()
        val login = LoginFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.main_container, login).commit()

    }
}