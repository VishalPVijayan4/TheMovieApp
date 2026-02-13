package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.databinding.FragmentProfileBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadProfile()

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.tvName.text = profile?.name?.ifBlank { "TMDB User" } ?: "TMDB User"
            binding.tvUsername.text = "@${profile?.username ?: "unknown"}"
            binding.tvAccountId.text = "Account ID: ${profile?.id ?: "N/A"}"

            val avatarPath = profile?.avatar?.tmdb?.avatar_path
            val avatarUrl = if (!avatarPath.isNullOrBlank()) {
                "https://image.tmdb.org/t/p/w500$avatarPath"
            } else null
            Glide.with(this)
                .load(avatarUrl)
                .circleCrop()
                .into(binding.ivAvatar)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
