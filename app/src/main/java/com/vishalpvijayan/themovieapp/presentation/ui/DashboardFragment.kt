package com.vishalpvijayan.themovieapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.databinding.FragmentDashboardBinding
import com.vishalpvijayan.themovieapp.databinding.LayoutDashboardSectionBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.BannerAdapter
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.ViewMoreAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.AuthViewModel
import com.vishalpvijayan.themovieapp.presentation.viewmodel.DashboardViewModel
import com.vishalpvijayan.themovieapp.presentation.viewmodel.SectionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()
    private val dashboardViewModel: DashboardViewModel by viewModels()

    private lateinit var nowPlayingAdapter: MovieAdapter
    private lateinit var popularAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var upcomingAdapter: MovieAdapter
    private lateinit var bannerAdapter: BannerAdapter

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

        bannerAdapter = BannerAdapter { movie, posterView ->
            findNavController().navigate(
                DashboardFragmentDirections.actionDashboardScreenToMovieDetailFragment(movie.id),
                FragmentNavigatorExtras(posterView to "poster_${movie.id}")
            )
        }
        binding.vpMovieCarousel.adapter = bannerAdapter
        binding.vpMovieCarousel.offscreenPageLimit = 1
        binding.vpMovieCarousel.clipToPadding = true

        nowPlayingAdapter = createAdapter()
        popularAdapter = createAdapter()
        topRatedAdapter = createAdapter()
        upcomingAdapter = createAdapter()

        setupSection(binding.sectionNowPlaying, nowPlayingAdapter, "now_playing")
        setupSection(binding.sectionPopular, popularAdapter, "popular")
        setupSection(binding.sectionTopRated, topRatedAdapter, "top_rated")
        setupSection(binding.sectionUpcoming, upcomingAdapter, "upcoming")

        binding.profileHeaderCard.setOnClickListener {
            findNavController().navigate(DashboardFragmentDirections.actionDashboardScreenToProfileFragment())
        }

        authViewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (!loggedIn) {
                findNavController().navigate(DashboardFragmentDirections.actionDashboardScreenToLoginFragment())
            }
        }

        dashboardViewModel.carouselMovies.observe(viewLifecycleOwner) {
            bannerAdapter.submitList(it)
        }

        dashboardViewModel.sections.observe(viewLifecycleOwner) { sections ->
            renderSection(binding.sectionNowPlaying, nowPlayingAdapter, sections["now_playing"])
            renderSection(binding.sectionPopular, popularAdapter, sections["popular"])
            renderSection(binding.sectionTopRated, topRatedAdapter, sections["top_rated"])
            renderSection(binding.sectionUpcoming, upcomingAdapter, sections["upcoming"])
        }

        dashboardViewModel.accountDetails.observe(viewLifecycleOwner) { account ->
            val name = account?.name?.ifBlank { account.username ?: "User" } ?: "User"
            binding.tvWelcome.text = "Welcome back, $name"
            val avatarPath = account?.avatar?.tmdb?.avatar_path
            val avatarUrl = avatarPath?.let { "https://image.tmdb.org/t/p/w200$it" }
            Glide.with(this).load(avatarUrl).circleCrop().into(binding.ivAvatar)
        }
    }

    private fun setupSection(section: LayoutDashboardSectionBinding, adapter: MovieAdapter, category: String) {
        section.rvMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val viewMoreAdapter = ViewMoreAdapter {
            val title = section.tvSectionTitle.text.toString()
            findNavController().navigate(
                DashboardFragmentDirections.actionDashboardScreenToMovieListFragment(category, title)
            )
        }
        section.rvMovies.adapter = ConcatAdapter(adapter, viewMoreAdapter)
        section.btnReload.setOnClickListener { dashboardViewModel.reloadSection(category) }
    }

    private fun renderSection(section: LayoutDashboardSectionBinding, adapter: MovieAdapter, state: SectionState?) {
        state ?: return
        section.tvSectionTitle.text = state.title
        section.shimmerContainer.isVisible = state.isLoading
        section.errorContainer.isVisible = !state.error.isNullOrBlank()
        section.tvError.text = state.error
        section.rvMovies.isVisible = !state.isLoading && state.error.isNullOrBlank()
        if (state.isLoading) section.shimmerContainer.startShimmer() else section.shimmerContainer.stopShimmer()
        adapter.submitList(state.movies)
    }

    private fun createAdapter(): MovieAdapter = MovieAdapter(onItemClick = { movie, posterView ->
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardScreenToMovieDetailFragment(movie.id),
            FragmentNavigatorExtras(posterView to "poster_${movie.id}")
        )
    })

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
