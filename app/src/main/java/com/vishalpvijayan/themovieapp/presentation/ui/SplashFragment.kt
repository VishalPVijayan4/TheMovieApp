package com.vishalpvijayan.themovieapp.presentation.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
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
        icon.scaleX = 0.5f
        icon.scaleY = 0.5f
        icon.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(900).setInterpolator(AccelerateDecelerateInterpolator()).start()

        ObjectAnimator.ofPropertyValuesHolder(
            icon,
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.06f, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.06f, 1f)
        ).apply {
            duration = 1200
            repeatCount = ObjectAnimator.INFINITE
            startDelay = 900
            start()
        }

        appName.alpha = 0f
        appName.translationY = 40f
        appName.animate().alpha(1f).translationY(0f).setStartDelay(220).setDuration(700).start()

        Handler(Looper.getMainLooper()).postDelayed({
            val action = if (sessionManager.isLoggedIn()) {
                SplashFragmentDirections.actionSplashScreenToDashboardScreen()
            } else {
                SplashFragmentDirections.actionSplashScreenToLoginFragment()
            }
            findNavController().navigate(action)
        }, 1900)
    }
}
