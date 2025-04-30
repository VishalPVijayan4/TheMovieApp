package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import com.vishalpvijayan.themovieapp.presentation.ui.adapter.OfflineUserAdapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vishalpvijayan.themovieapp.databinding.FragmentOfflineUsersBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.OfflineUserViewModel
import com.vishalpvijayan.themovieapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OfflineUsersFragment : Fragment() {

    private var _binding: FragmentOfflineUsersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: OfflineUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOfflineUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = OfflineUserAdapter { user ->
            viewModel.syncSingleUser(user)
        }
        binding.recyclerViewOfflineUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOfflineUsers.adapter = adapter


        viewModel.unsyncedUsers.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.getUnsyncedUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


/*@AndroidEntryPoint
class OfflineUsersFragment : Fragment() {

    private var _binding: FragmentOfflineUsersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OfflineUserViewModel by viewModels()
    private val adapter by lazy { OfflineUserAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfflineUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerViewOfflineUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOfflineUsers.adapter = adapter

        lifecycleScope.launch {
            viewModel.offlineUsers.collectLatest { users ->
                adapter.submitList(users)
            }
        }

        viewModel.fetchOfflineUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/
