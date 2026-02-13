package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vishalpvijayan.themovieapp.databinding.FragmentGenresBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.GenreAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.GenresViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenresFragment : Fragment() {

    private var _binding: FragmentGenresBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GenresViewModel by viewModels()

    private lateinit var movieAdapter: GenreAdapter
    private lateinit var tvAdapter: GenreAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGenresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = GenreAdapter { genre ->
            val category = "genre_movie_${genre.id}"
            findNavController().navigate(
                GenresFragmentDirections.actionGenresFragmentToMovieListFragment(category, "${genre.name} Movies")
            )
        }
        tvAdapter = GenreAdapter { genre ->
            val category = "genre_tv_${genre.id}"
            findNavController().navigate(
                GenresFragmentDirections.actionGenresFragmentToMovieListFragment(category, "${genre.name} TV")
            )
        }

        binding.rvMovieGenres.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovieGenres.adapter = movieAdapter
        binding.rvTvGenres.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTvGenres.adapter = tvAdapter

        viewModel.movieGenres.observe(viewLifecycleOwner) { movieAdapter.submitList(it) }
        viewModel.tvGenres.observe(viewLifecycleOwner) { tvAdapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
