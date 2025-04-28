package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadStateAdapter
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.databinding.FragmentUserListBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.UserLoadStateAdapter
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.UserPagingAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserListFragment : Fragment(R.layout.fragment_user_list) {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserPagingAdapter
    private lateinit var binding: FragmentUserListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter
        adapter = UserPagingAdapter()

        // Inflate the binding
        binding = FragmentUserListBinding.bind(view)

        // Set the adapter with LoadStateFooter
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = UserLoadStateAdapter { adapter.retry() }
        )

        binding.fabAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_addUserFragment)
        }

        // Collect and submit paging data
        lifecycleScope.launchWhenStarted {
            viewModel.users.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}


