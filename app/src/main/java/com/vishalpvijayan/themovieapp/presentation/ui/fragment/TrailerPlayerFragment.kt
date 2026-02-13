package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
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
        binding.webTrailer.settings.javaScriptEnabled = true
        binding.webTrailer.webChromeClient = WebChromeClient()
        val html = """
            <html><body style=\"margin:0;background:black;\">
            <iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/$trailerKey?autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>
            </body></html>
        """.trimIndent()
        binding.webTrailer.loadData(html, "text/html", "utf-8")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
