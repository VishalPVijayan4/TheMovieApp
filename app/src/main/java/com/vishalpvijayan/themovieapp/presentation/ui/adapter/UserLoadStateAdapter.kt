package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.themovieapp.databinding.LoadStateFooterBinding


class UserLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<UserLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        // Bind the load state to the view
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        // Inflate the layout for load state
        val binding = LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    class LoadStateViewHolder(private val binding: LoadStateFooterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            // Handle loading, error, or completed states
            binding.progressBar.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            binding.retryButton.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
            binding.retryButton.setOnClickListener { /* retry the action */ }
            binding.errorMsg.text = (loadState as? LoadState.Error)?.error?.message
        }
    }
}
