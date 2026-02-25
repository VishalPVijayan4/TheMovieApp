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
import com.vishalpvijayan.themovieapp.databinding.FragmentPersonDetailBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.MovieAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.PersonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDetailFragment : Fragment() {

    private var _binding: FragmentPersonDetailBinding? = null
    private val binding get() = _binding!!
    private val args: PersonDetailFragmentArgs by navArgs()
    private val viewModel: PersonDetailViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter { movie ->
            findNavController().navigate(PersonDetailFragmentDirections.actionPersonDetailFragmentToMovieDetailFragment(movie.id))
        }
        binding.rvPersonMovies.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPersonMovies.adapter = adapter

        viewModel.load(args.personId)
        binding.contentGroup.alpha = 0f
        binding.contentGroup.translationY = 40f

        viewModel.person.observe(viewLifecycleOwner) { person ->
            person ?: return@observe
            binding.tvPersonName.text = person.name ?: "Unknown"
            binding.tvPersonMeta.text = listOfNotNull(person.known_for_department, person.birthday, person.place_of_birth).joinToString(" • ")
            binding.tvPersonBio.text = person.biography?.ifBlank { "No biography available" } ?: "No biography available"
            Glide.with(this).load("https://image.tmdb.org/t/p/w500${person.profile_path}").into(binding.ivPerson)
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            binding.tvFilmographyTitle.text = "Filmography (${movies.size})"
            adapter.submitList(movies)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            if (!it) {
                binding.contentGroup.animate().alpha(1f).translationY(0f).setDuration(300).start()
            }
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
