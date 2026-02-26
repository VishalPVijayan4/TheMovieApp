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
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.themovieapp.databinding.FragmentMediaCollectionBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.ProfileFavoriteAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.MediaCollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaCollectionFragment : Fragment() {

    private var _binding: FragmentMediaCollectionBinding? = null
    private val binding get() = _binding!!
    private val args: MediaCollectionFragmentArgs by navArgs()
    private val viewModel: MediaCollectionViewModel by viewModels()
    private lateinit var movieAdapter: ProfileFavoriteAdapter
    private lateinit var tvAdapter: ProfileFavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMediaCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = if (args.collectionType == "favorite") "Favorites" else "Watchlist"

        movieAdapter = ProfileFavoriteAdapter { movie, posterView ->
            findNavController().navigate(
                MediaCollectionFragmentDirections.actionMediaCollectionFragmentToMovieDetailFragment(movie.id),
                FragmentNavigatorExtras(posterView to "poster_${movie.id}")
            )
        }
        tvAdapter = ProfileFavoriteAdapter { tv, _ ->
            findNavController().navigate(MediaCollectionFragmentDirections.actionMediaCollectionFragmentToTvSeriesDetailFragment(tv.id))
        }

        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMovies.adapter = movieAdapter
        binding.rvTv.adapter = tvAdapter

        viewModel.load(args.collectionType)

        viewModel.movies.observe(viewLifecycleOwner) { movieAdapter.submitList(it) }
        viewModel.tv.observe(viewLifecycleOwner) { tvAdapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) { binding.progressBar.isVisible = it }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.tvError.isVisible = !it.isNullOrBlank()
            binding.tvError.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
