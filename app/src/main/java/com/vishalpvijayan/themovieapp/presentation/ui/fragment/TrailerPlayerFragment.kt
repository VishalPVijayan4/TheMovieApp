package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.vishalpvijayan.themovieapp.databinding.FragmentTrailerPlayerBinding

class TrailerPlayerFragment : Fragment() {

    private var _binding: FragmentTrailerPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTrailerPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trailerKey = arguments?.getString("trailerKey").orEmpty()
        val youtubeWatchUrl = "https://www.youtube.com/watch?v=$trailerKey"

        binding.webTrailer.settings.javaScriptEnabled = true
        binding.webTrailer.settings.domStorageEnabled = true
        binding.webTrailer.settings.mediaPlaybackRequiresUserGesture = false
        binding.webTrailer.webChromeClient = WebChromeClient()
        binding.webTrailer.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }

        binding.webTrailer.loadUrl(youtubeWatchUrl)

        binding.btnOpenYouTube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeWatchUrl)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
