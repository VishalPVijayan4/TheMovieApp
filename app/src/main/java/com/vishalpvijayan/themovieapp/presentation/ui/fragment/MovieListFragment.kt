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
import com.vishalpvijayan.themovieapp.databinding.FragmentMovieListBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel: MovieViewModel by viewModels()
    private val args: MovieListFragmentArgs by navArgs()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter(onItemClick = { movie ->
            val isTvCategory = args.category.startsWith("tv_") || args.category.startsWith("genre_tv_")
            if (isTvCategory) {
                val action = MovieListFragmentDirections.actionMovieListFragmentToTvSeriesDetailFragment(movie.id)
                findNavController().navigate(action)
            } else {
                val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie.id)
                findNavController().navigate(action)
            }
        })

        binding.txtTreanding.text = args.title
        binding.rvMovieList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovieList.adapter = adapter

        viewModel.trendingMovies.observe(viewLifecycleOwner) { movieList ->
            adapter.submitList(movieList)
        }

        viewModel.loadMovies(args.category)

        binding.btnLoadMore.setOnClickListener {
            viewModel.loadMoreMovies()
        }
    }
}
