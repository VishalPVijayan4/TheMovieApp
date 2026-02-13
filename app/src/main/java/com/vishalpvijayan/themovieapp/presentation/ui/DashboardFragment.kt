package com.vishalpvijayan.themovieapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.databinding.FragmentDashboardBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardNowPlaying.setOnClickListener { navigateToMovies("now_playing", "Now Playing") }
        binding.cardPopular.setOnClickListener { navigateToMovies("popular", "Popular") }
        binding.cardTopRated.setOnClickListener { navigateToMovies("top_rated", "Top Rated") }
        binding.cardUpcoming.setOnClickListener { navigateToMovies("upcoming", "Upcoming") }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
        }

        authViewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (!loggedIn) {
                val action = DashboardFragmentDirections.actionDashboardScreenToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun navigateToMovies(category: String, title: String) {
        val action = DashboardFragmentDirections.actionDashboardScreenToMovieListFragment(category, title)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
