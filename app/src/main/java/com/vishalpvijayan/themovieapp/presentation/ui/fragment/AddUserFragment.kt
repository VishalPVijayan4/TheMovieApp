package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.databinding.FragmentAddUserBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    private lateinit var binding: FragmentAddUserBinding
    private val viewModel: UserViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUserBinding.bind(view)
        val name = binding.inputName.text.toString().trim()
        val job = binding.inputJob.text.toString().trim()
        binding.btnSubmit.setOnClickListener {
            val name = binding.inputName.text.toString()
            val job = binding.inputJob.text.toString()
            viewModel.addUser(name, job)
            // Go back after submitting
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

}
