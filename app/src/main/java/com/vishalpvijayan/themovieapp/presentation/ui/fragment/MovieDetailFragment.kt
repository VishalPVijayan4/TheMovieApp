package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.databinding.FragmentMovieDetailBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.CreditsAdapter
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var similarAdapter: MovieAdapter
    private lateinit var creditsAdapter: CreditsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        similarAdapter = MovieAdapter(onItemClick = { movie ->
            val action = MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(movie.id)
            findNavController().navigate(action)
        })
        binding.rvSimilarMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSimilarMovies.adapter = similarAdapter

        creditsAdapter = CreditsAdapter { person ->
            findNavController().navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToPersonDetailFragment(person.id))
        }
        binding.rvCredits.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCredits.adapter = creditsAdapter

        viewModel.getMovieDetail(args.movieId)
        binding.contentGroup.alpha = 0f
        binding.contentGroup.translationY = 40f

        binding.btnFavorite.setOnClickListener {
            viewModel.toggleFavorite(args.movieId)
        }
        binding.btnWatchlist.setOnClickListener {
            viewModel.toggleWatchlist(args.movieId)
        }

        viewModel.favorite.observe(viewLifecycleOwner) {
            binding.btnFavorite.setImageResource(
                if (it) com.vishalpvijayan.themovieapp.R.drawable.ic_favorite_filled
                else com.vishalpvijayan.themovieapp.R.drawable.ic_favorite_outline
            )
        }
        viewModel.watchlist.observe(viewLifecycleOwner) {
            binding.btnWatchlist.setImageResource(
                if (it) com.vishalpvijayan.themovieapp.R.drawable.ic_watchlist_filled
                else com.vishalpvijayan.themovieapp.R.drawable.ic_watchlist_outline
            )
        }

        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            movie?.let {
                binding.movieTitle.text = it.title ?: "Untitled"
                binding.movieTagline.text = it.tagline ?: ""
                binding.movieDescription.text = it.overview ?: "No description available"

                val meta = listOfNotNull(
                    it.release_date,
                    it.runtime?.let { runtime -> "${runtime} min" },
                    it.vote_average?.let { rating -> "⭐ %.1f".format(rating) },
                    it.original_language?.uppercase()
                ).joinToString(" • ")
                binding.movieMeta.text = meta
                binding.movieGenres.text = it.genres?.joinToString("  •  ") { genre -> genre.name }.orEmpty()

                val posterUrl = "https://image.tmdb.org/t/p/w500${it.poster_path}"
                val backdropUrl = "https://image.tmdb.org/t/p/w780${it.backdrop_path}"
                Glide.with(requireContext()).load(posterUrl).into(binding.moviePoster)
                Glide.with(requireContext()).load(backdropUrl).into(binding.movieBackdrop)
            }
        }

        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            val trailer = videos.firstOrNull { it.type == "Trailer" && it.site == "YouTube" }
            binding.movieVideos.text = if (trailer != null) {
                "Trailer: ${trailer.name ?: "Available"}"
            } else {
                "Trailer: Not available"
            }
        }

        viewModel.trailerKey.observe(viewLifecycleOwner) { key ->
            binding.btnPlayTrailer.visibility = if (key.isNullOrBlank()) View.GONE else View.VISIBLE
            binding.btnPlayTrailer.setOnClickListener {
                if (!key.isNullOrBlank()) {
                    findNavController().navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToTrailerPlayerFragment(key))
                }
            }
        }

        viewModel.imagesInfo.observe(viewLifecycleOwner) {
            binding.movieImagesInfo.text = it
        }

        viewModel.watchProvidersText.observe(viewLifecycleOwner) {
            binding.tvWatchProviders.text = it
        }

        viewModel.watchProvidersLink.observe(viewLifecycleOwner) { link ->
            binding.btnOpenProvider.visibility = if (link.isNullOrBlank()) View.GONE else View.VISIBLE
            binding.btnOpenProvider.setOnClickListener {
                if (!link.isNullOrBlank()) {
                    findNavController().navigate(MovieDetailFragmentDirections.actionMovieDetailFragmentToWebViewFragment(link))
                }
            }
        }

        viewModel.credits.observe(viewLifecycleOwner) { creditsAdapter.submitList(it) }

        viewModel.similarMovies.observe(viewLifecycleOwner) { similar ->
            similarAdapter.submitList(similar)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            if (!isLoading) {
                binding.contentGroup.animate().alpha(1f).translationY(0f).setDuration(300).start()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            binding.errorTextView.visibility = if (errorMsg != null) View.VISIBLE else View.GONE
            binding.errorTextView.text = errorMsg
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
