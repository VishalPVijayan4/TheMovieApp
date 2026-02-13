package com.vishalpvijayan.themovieapp.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.databinding.FragmentLoginBinding
import com.vishalpvijayan.themovieapp.presentation.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text?.toString().orEmpty().trim()
            val password = binding.etPassword.text?.toString().orEmpty()
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(requireContext(), "Enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.login(username, password)
            }
        }


        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToWebViewFragment("https://www.themoviedb.org/signup")
            )
        }
        authViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !it
        }

        authViewModel.authMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                val action = LoginFragmentDirections.actionLoginFragmentToDashboardScreen()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
