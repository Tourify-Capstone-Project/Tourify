package com.capstone.project.tourify.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.capstone.project.tourify.R
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        val bottomBar: AnimatedBottomBar = findViewById(R.id.bottom_nav)
        setupBottomNavigationBar(bottomBar, navController)
    }

    private fun setupBottomNavigationBar(
        bottomBar: AnimatedBottomBar,
        navController: NavController
    ) {
        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                navController.navigate(newTab.id)
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {

            }
        })

    }
}