package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.databinding.FragmentMovieDetailBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs() // SafeArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovieDetail(args.movieId)

        viewModel.movie.observe(viewLifecycleOwner) { movie ->
            movie?.let {
                binding.movieTitle.text = it.title
                binding.movieRating.text = "Rating: ${it.vote_average ?: "N/A"}"
                binding.movieDescription.text = it.overview ?: "No description available"

                val imageUrl = "https://image.tmdb.org/t/p/w500${it.poster_path}"
                Glide.with(requireContext())
                    .load(imageUrl)
                    .into(binding.moviePoster)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
