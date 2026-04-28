package com.hassanmir.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Load Feed as default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FeedFragment())
            .commit()

        findViewById<BottomNavigationView>(R.id.bottomNav).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feedFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, FeedFragment()).commit()
                R.id.profileFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ProfileFragment()).commit()
                R.id.logoutFragment -> supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LogoutFragment()).commit()
            }
            true
        }
    }
}