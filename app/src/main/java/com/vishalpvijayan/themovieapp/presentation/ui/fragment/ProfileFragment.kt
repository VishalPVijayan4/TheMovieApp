package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.databinding.FragmentProfileBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.AuthViewModel
import com.vishalpvijayan.themovieapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadProfile()

        binding.btnOpenFavorites.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMediaCollectionFragment("favorite"))
        }
        binding.btnOpenWatchlist.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMediaCollectionFragment("watchlist"))
        }

        binding.tvVersion.text = "Version 1.0.0"

        binding.btnAbout.setOnClickListener {
            showScrollableDialog(
                "About",
                """The Movie App is a movie discovery application that allows users to browse trending movies and TV shows, search for titles, and view detailed information such as ratings, overview, release date, and cast.

The app focuses on simplicity, speed, and clean UI rather than streaming or downloading content.

Important distinction:
👉 This app does NOT stream movies.

This application uses The Movie Database (TMDB) API to fetch movie information such as ratings, posters, cast, and release details.

TMDB is a community-driven online movie and TV database that provides developers access to structured film metadata through a public API.
The API allows applications to programmatically retrieve movie data and images for display purposes.

This app only displays publicly available information and does not host or stream any copyrighted content."""
            )
        }

        binding.btnPrivacy.setOnClickListener {
            showScrollableDialog(
                "Privacy Policy",
                "This app uses TMDB APIs to fetch movie metadata. Login sessions are stored locally on your device. No additional personal data is sold or shared by the app."
            )
        }

        binding.btnContact.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/vishal-p-vijayan-128429132/")))
        }

        binding.btnLogout.setOnClickListener { authViewModel.logout() }

        authViewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (!loggedIn) {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
            }
        }

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.tvName.text = profile?.name?.ifBlank { "TMDB User" } ?: "TMDB User"
            binding.tvUsername.text = "@${profile?.username ?: "unknown"}"
            binding.tvAccountId.text = "Account ID: ${profile?.id ?: "N/A"}"

            val avatarPath = profile?.avatar?.tmdb?.avatar_path
            val avatarUrl = if (!avatarPath.isNullOrBlank()) {
                "https://image.tmdb.org/t/p/w500$avatarPath"
            } else null
            Glide.with(this)
                .load(avatarUrl)
                .circleCrop()
                .into(binding.ivAvatar)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showScrollableDialog(title: String, message: String) {
        val textView = TextView(requireContext()).apply {
            text = message
            setPadding(32, 24, 32, 24)
            movementMethod = ScrollingMovementMethod()
        }
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(textView)
            .setPositiveButton("Close", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
