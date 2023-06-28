package com.example.opsc7311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val homeFragment = HomeFragment()
        val projectFragment = ViewProject()

        setCurrentFragment(homeFragment)

        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navigation.setOnItemReselectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.project -> setCurrentFragment(projectFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flNavigation, fragment)
            commit()
        }
}