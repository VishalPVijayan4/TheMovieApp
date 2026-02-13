package com.vishalpvijayan.themovieapp.presentation.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = setOf(
        R.id.dashboardScreen,
        R.id.genresFragment,
        R.id.tvFragment,
        R.id.searchFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavView, navController)
        val appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        NavigationUI.setupWithNavController(binding.topToolbar, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = destination.id in topLevelDestinations
            val hideBars = destination.id == R.id.splashScreen || destination.id == R.id.loginFragment

            binding.bottomNavView.visibility = if (hideBars || !isTopLevel) View.GONE else View.VISIBLE
            binding.topToolbar.visibility = if (hideBars || isTopLevel) View.GONE else View.VISIBLE
            binding.topToolbar.title = destination.label
        }
    }
}
