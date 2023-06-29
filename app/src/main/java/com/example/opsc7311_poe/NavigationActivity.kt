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

        val navigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.project -> setCurrentFragment(projectFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        if (fragment is HomeFragment) {
            supportFragmentManager.commit {
                setCustomAnimations(
                    R.anim.from_left,
                    R.anim.to_right,
                    R.anim.from_right,
                    R.anim.to_left
                )
                replace(R.id.flNavigation, fragment)
                addToBackStack(null)
            }
        } else if (fragment is ViewProject) {
                supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.from_right,
                        R.anim.to_left,
                        R.anim.from_left,
                        R.anim.to_right
                    )
                    replace(R.id.flNavigation, fragment)
                    addToBackStack(null)
                }
            }
        }
}