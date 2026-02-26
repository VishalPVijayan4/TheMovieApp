package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.themovieapp.databinding.FragmentTvBinding
import com.vishalpvijayan.themovieapp.databinding.LayoutDashboardSectionBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.BannerAdapter
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.ViewMoreAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.SectionState
import com.vishalpvijayan.themovieapp.presentation.viewmodel.TvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvFragment : Fragment() {

    private var _binding: FragmentTvBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TvViewModel by viewModels()

    private lateinit var airingAdapter: MovieAdapter
    private lateinit var onAirAdapter: MovieAdapter
    private lateinit var popularAdapter: MovieAdapter
    private lateinit var topRatedAdapter: MovieAdapter
    private lateinit var bannerAdapter: BannerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTvBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerAdapter = BannerAdapter { tv, _ ->
            val action = TvFragmentDirections.actionTvFragmentToTvSeriesDetailFragment(tv.id)
            findNavController().navigate(action)
        }
        binding.vpTvCarousel.adapter = bannerAdapter
        binding.vpTvCarousel.offscreenPageLimit = 2

        airingAdapter = createAdapter()
        onAirAdapter = createAdapter()
        popularAdapter = createAdapter()
        topRatedAdapter = createAdapter()

        setupSection(binding.sectionAiringToday, airingAdapter, "tv_airing_today")
        setupSection(binding.sectionOnTheAir, onAirAdapter, "tv_on_the_air")
        setupSection(binding.sectionPopularTv, popularAdapter, "tv_popular")
        setupSection(binding.sectionTopRatedTv, topRatedAdapter, "tv_top_rated")

        viewModel.carouselShows.observe(viewLifecycleOwner) { bannerAdapter.submitList(it) }

        viewModel.sections.observe(viewLifecycleOwner) { sections ->
            renderSection(binding.sectionAiringToday, airingAdapter, sections["tv_airing_today"])
            renderSection(binding.sectionOnTheAir, onAirAdapter, sections["tv_on_the_air"])
            renderSection(binding.sectionPopularTv, popularAdapter, sections["tv_popular"])
            renderSection(binding.sectionTopRatedTv, topRatedAdapter, sections["tv_top_rated"])
        }
    }

    private fun setupSection(section: LayoutDashboardSectionBinding, adapter: MovieAdapter, category: String) {
        section.rvMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        section.rvMovies.setHasFixedSize(true)
        section.rvMovies.itemAnimator = null
        section.rvMovies.setItemViewCacheSize(14)
        val viewMoreAdapter = ViewMoreAdapter {
            val action = TvFragmentDirections.actionTvFragmentToMovieListFragment(category, section.tvSectionTitle.text.toString())
            findNavController().navigate(action)
        }
        section.rvMovies.adapter = ConcatAdapter(adapter, viewMoreAdapter)
        section.btnReload.setOnClickListener { viewModel.reloadSection(category) }
    }

    private fun renderSection(section: LayoutDashboardSectionBinding, adapter: MovieAdapter, state: SectionState?) {
        state ?: return
        section.tvSectionTitle.text = state.title
        val showShimmer = state.isLoading && state.movies.isEmpty()
        section.shimmerContainer.isVisible = showShimmer
        section.errorContainer.isVisible = !showShimmer && !state.error.isNullOrBlank() && state.movies.isEmpty()
        section.tvError.text = state.error
        section.rvMovies.isVisible = !showShimmer
        if (showShimmer) section.shimmerContainer.startShimmer() else section.shimmerContainer.stopShimmer()
        adapter.submitList(state.movies)
        if (state.movies.isNotEmpty()) {
            section.rvMovies.post { section.rvMovies.scrollToPosition(0) }
        }
    }

    private fun createAdapter(): MovieAdapter = MovieAdapter(onItemClick = { tv, _ ->
        val action = TvFragmentDirections.actionTvFragmentToTvSeriesDetailFragment(tv.id)
        findNavController().navigate(action)
    })

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
