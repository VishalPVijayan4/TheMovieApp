package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.databinding.FragmentTvSeriesDetailBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.TvSeriesDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSeriesDetailFragment : Fragment() {

    private var _binding: FragmentTvSeriesDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TvSeriesDetailFragmentArgs by navArgs()
    private val viewModel: TvSeriesDetailViewModel by viewModels()
    private lateinit var similarAdapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTvSeriesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        similarAdapter = MovieAdapter(onItemClick = { tv ->
            findNavController().navigate(TvSeriesDetailFragmentDirections.actionTvSeriesDetailFragmentSelf(tv.id))
        })
        binding.rvSimilarTv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSimilarTv.adapter = similarAdapter

        viewModel.load(args.seriesId)

        viewModel.detail.observe(viewLifecycleOwner) { d ->
            d ?: return@observe
            binding.tvTitle.text = d.name ?: "Untitled"
            binding.tvMeta.text = listOfNotNull(
                d.first_air_date,
                d.status,
                d.vote_average?.let { "⭐ %.1f".format(it) },
                d.number_of_seasons?.let { "$it seasons" },
                d.number_of_episodes?.let { "$it episodes" }
            ).joinToString(" • ")
            binding.tvOverview.text = d.overview ?: "No overview"
            binding.tvGenres.text = d.genres?.joinToString(" • ") { it.name }.orEmpty()
            Glide.with(this).load("https://image.tmdb.org/t/p/w780${d.backdrop_path}").into(binding.tvBackdrop)
            Glide.with(this).load("https://image.tmdb.org/t/p/w500${d.poster_path}").into(binding.tvPoster)
        }

        viewModel.videos.observe(viewLifecycleOwner) {
            val trailer = it.firstOrNull { v -> v.type == "Trailer" && v.site == "YouTube" }
            binding.tvVideos.text = if (trailer != null) "Trailer: ${trailer.name ?: "Available"}" else "Trailer: Not available"
        }

        viewModel.episodeGroups.observe(viewLifecycleOwner) {
            binding.tvEpisodeGroups.text = "Episode groups: ${it.size}"
        }

        viewModel.similar.observe(viewLifecycleOwner) {
            similarAdapter.submitList(it)
        }

        viewModel.watchProvidersText.observe(viewLifecycleOwner) {
            binding.tvWatchProviders.text = it
        }

        viewModel.watchProvidersLink.observe(viewLifecycleOwner) { link ->
            binding.btnOpenProvider.visibility = if (link.isNullOrBlank()) View.GONE else View.VISIBLE
            binding.btnOpenProvider.setOnClickListener {
                if (!link.isNullOrBlank()) {
                    findNavController().navigate(TvSeriesDetailFragmentDirections.actionTvSeriesDetailFragmentToWebViewFragment(link))
                }
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorTextView.visibility = if (it.isNullOrBlank()) View.GONE else View.VISIBLE
            binding.errorTextView.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
