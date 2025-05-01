package com.vishalpvijayan.themovieapp.presentation.ui.fragment

/*import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.data.local.datasource.UserLocalDataSource
import com.vishalpvijayan.themovieapp.databinding.FragmentAddUserBinding
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.presentation.viewmodel.UserViewModel
import com.vishalpvijayan.themovieapp.workers.SyncUserWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.toString

@AndroidEntryPoint

class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    private lateinit var binding: FragmentAddUserBinding
    private val viewModel: UserViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUserBinding.bind(view)
        binding.btnSubmit.setOnClickListener {
            val name = binding.inputName.text.toString()
            val job = binding.inputJob.text.toString()
            viewModel.addUser(user = name, position = job)
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

}*/



import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.databinding.FragmentAddUserBinding
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.presentation.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    private lateinit var binding: FragmentAddUserBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddUserBinding.bind(view)

        binding.btnSubmit.setOnClickListener {
            val name = binding.inputName.text.toString().trim()
            val job = binding.inputJob.text.toString().trim()

            if (name.isEmpty() || job.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both name and job", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                id = 0, // assuming auto-generated
                fullName = name,
                position = job,
                isSynced = false
            )

            viewModel.addUser(user)



            // Hide keyboard
            val imm = getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)

            // Navigate back
            findNavController().popBackStack()
        }
    }
}
