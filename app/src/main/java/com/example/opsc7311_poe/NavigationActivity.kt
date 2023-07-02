package com.example.opsc7311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val projectFragment = ViewProject()
    var fragments = mutableListOf<Fragment>(homeFragment, projectFragment)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val homeFragment = HomeFragment()
        val projectFragment = ViewProject()

        supportFragmentManager.commit {
            setCustomAnimations(R.anim.fade_in, R.anim.slide_in, R.anim.fade_out, R.anim.slide_out)
            replace(R.id.flNavigation, homeFragment)
            addToBackStack(null)
        }


    }


}