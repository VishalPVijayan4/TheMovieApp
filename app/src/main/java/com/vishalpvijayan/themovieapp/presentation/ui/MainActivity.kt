package com.vishalpvijayan.themovieapp.presentation.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
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

    private var noInternetDialog: AlertDialog? = null
    private var connectivityCallback: ConnectivityManager.NetworkCallback? = null

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

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            val isTopLevel = destination.id in topLevelDestinations
            val hideBars = destination.id == R.id.splashScreen || destination.id == R.id.loginFragment

            binding.bottomNavView.visibility = if (hideBars || !isTopLevel) View.GONE else View.VISIBLE
            binding.topToolbar.visibility = if (hideBars || isTopLevel) View.GONE else View.VISIBLE

            val dynamicTitle = if (destination.id == R.id.movieListFragment) arguments?.getString("title") else null
            binding.topToolbar.title = dynamicTitle ?: destination.label

            binding.screenTransitionLoader.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.screenTransitionLoader.visibility = View.GONE
            }, 350)
        }

        registerConnectivityMonitoring()
    }

    private fun registerConnectivityMonitoring() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                runOnUiThread { noInternetDialog?.dismiss() }
            }

            override fun onLost(network: Network) {
                runOnUiThread {
                    if (!isInternetAvailable(connectivityManager)) showNoInternetDialog()
                }
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, connectivityCallback!!)

        if (!isInternetAvailable(connectivityManager)) {
            showNoInternetDialog()
        }
    }

    private fun isInternetAvailable(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showNoInternetDialog() {
        if (noInternetDialog?.isShowing == true) return
        noInternetDialog = AlertDialog.Builder(this)
            .setTitle("No internet connection")
            .setMessage("Please connect to the internet to continue using The Movie App.")
            .setCancelable(false)
            .setPositiveButton("Retry") { dialog, _ ->
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (isInternetAvailable(cm)) dialog.dismiss()
            }
            .create()
        noInternetDialog?.show()
    }

    override fun onDestroy() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityCallback?.let { connectivityManager.unregisterNetworkCallback(it) }
        connectivityCallback = null
        super.onDestroy()
    }
}
