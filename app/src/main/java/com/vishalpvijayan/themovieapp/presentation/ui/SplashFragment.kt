package com.vishalpvijayan.themovieapp.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.R

@SuppressLint("CustomSplashScreen")
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            val action = SplashFragmentDirections.actionSplashScreenToUserListFragment()
            findNavController().navigate(action)
        }, 2000)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}

