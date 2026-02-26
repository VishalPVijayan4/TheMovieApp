package com.vishalpvijayan.themovieapp.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.utilis.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val icon = view.findViewById<ImageView>(R.id.imageView2)
        val appName = view.findViewById<TextView>(R.id.txtAppName)

        icon.alpha = 0f
        icon.scaleX = 0.7f
        icon.scaleY = 0.7f
        icon.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(700).start()
        appName.alpha = 0f
        appName.translationY = 30f
        appName.animate().alpha(1f).translationY(0f).setStartDelay(250).setDuration(500).start()

        Handler(Looper.getMainLooper()).postDelayed({
            val action = if (sessionManager.isLoggedIn()) {
                SplashFragmentDirections.actionSplashScreenToDashboardScreen()
            } else {
                SplashFragmentDirections.actionSplashScreenToLoginFragment()
            }
            findNavController().navigate(action)
        }, 1700)
    }
}
