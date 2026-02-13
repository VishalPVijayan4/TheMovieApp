package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.themovieapp.databinding.ItemViewMoreBinding

class ViewMoreAdapter(
    private val onViewMoreClick: () -> Unit
) : RecyclerView.Adapter<ViewMoreAdapter.ViewMoreViewHolder>() {

    inner class ViewMoreViewHolder(private val binding: ItemViewMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener { onViewMoreClick() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewMoreViewHolder {
        val binding = ItemViewMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewMoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewMoreViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = 1
}
