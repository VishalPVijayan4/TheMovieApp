package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.databinding.FragmentMovieListBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel: MovieViewModel by viewModels()
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

        // Initialize the adapter with an onItemClick listener
        adapter = MovieAdapter(onItemClick = { movie ->
            // Show a toast when a movie is clicked
            Toast.makeText(requireContext(), "Clicked: ${movie.title}", Toast.LENGTH_SHORT).show()

            // Navigate to the Movie Detail Fragment with the movie's ID or other info
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)
        })

        // Set the adapter for the RecyclerView
        binding.rvMovieList.adapter = adapter

        // Observe the movie list from the ViewModel
        viewModel.trendingMovies.observe(viewLifecycleOwner) { movieList ->
            adapter.submitList(movieList)  // Submit the list to the adapter
        }

        // Trigger the API call to load trending movies
        viewModel.loadTrendingMovies()
    }
}
