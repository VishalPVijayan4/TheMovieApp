package com.vishalpvijayan.themovieapp.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.R

@SuppressLint("CustomSplashScreen")
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = requireContext().getSharedPreferences("tmdb_session", android.content.Context.MODE_PRIVATE)
            val sessionId = prefs.getString("session_id", null)
            val action = if (sessionId.isNullOrBlank()) {
                SplashFragmentDirections.actionSplashScreenToLoginFragment()
            } else {
                SplashFragmentDirections.actionSplashScreenToDashboardScreen()
            }
            findNavController().navigate(action)
        }, 1500)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}
