package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.vishalpvijayan.themovieapp.databinding.FragmentMovieListBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieGridAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel: MovieViewModel by viewModels()
    private val args: MovieListFragmentArgs by navArgs()
    private lateinit var adapter: MovieGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieGridAdapter { movie, posterView ->
            val isTvCategory = args.category.startsWith("tv_") || args.category.startsWith("genre_tv_")
            if (isTvCategory) {
                findNavController().navigate(MovieListFragmentDirections.actionMovieListFragmentToTvSeriesDetailFragment(movie.id), FragmentNavigatorExtras(posterView to "poster_${movie.id}"))
            } else {
                findNavController().navigate(MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie.id), FragmentNavigatorExtras(posterView to "poster_${movie.id}"))
            }
        }

        binding.txtTreanding.text = args.title
        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovieList.adapter = adapter

        viewModel.trendingMovies.observe(viewLifecycleOwner) { movieList ->
            adapter.submitList(movieList)
            binding.errorContainer.isVisible = movieList.isEmpty() && !viewModel.error.value.isNullOrBlank()
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.errorContainer.isVisible = !it.isNullOrBlank()
            binding.tvError.text = it
        }

        viewModel.loadMovies(args.category)

        binding.btnRetry.setOnClickListener {
            viewModel.loadMovies(args.category)
        }
    }
}
