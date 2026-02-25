package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.themovieapp.databinding.FragmentSearchBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.SearchAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SearchAdapter { item ->
            when (item.mediaType) {
                "tv" -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToTvSeriesDetailFragment(item.id))
                "person" -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToPersonDetailFragment(item.id))
                else -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(item.id))
            }
        }
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.adapter = adapter

        val triggerSearch = {
            viewModel.search(query = binding.etQuery.text?.toString().orEmpty())
        }

        binding.etQuery.doAfterTextChanged { triggerSearch() }

        viewModel.results.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.error.observe(viewLifecycleOwner) {
            binding.tvError.visibility = if (it.isNullOrBlank()) View.GONE else View.VISIBLE
            binding.tvError.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
