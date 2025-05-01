package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.themovieapp.data.local.mapper.toDomain
import com.vishalpvijayan.themovieapp.databinding.FragmentOfflineUsersBinding
import com.vishalpvijayan.themovieapp.presentation.ui.adapter.OfflineUserAdapter
import com.vishalpvijayan.themovieapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfflineUsersFragment : Fragment() {

    private lateinit var binding: FragmentOfflineUsersBinding
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: OfflineUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOfflineUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OfflineUserAdapter { userEntity ->
            // Sync this user when clicked
//            viewModel.syncSingleUser(userEntity.toDomain())
            viewModel.addUser(userEntity.toDomain())
        }

        binding.recyclerViewOfflineUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@OfflineUsersFragment.adapter
        }

        // Start observing offline users
        viewModel.getUnsyncedUsers()

        viewModel.unsyncedUsers.observe(viewLifecycleOwner) { users ->
            Log.d("OfflineUsersFragment", "Loaded ${users.size} offline users")
            adapter.submitList(users)
        }
    }
}



