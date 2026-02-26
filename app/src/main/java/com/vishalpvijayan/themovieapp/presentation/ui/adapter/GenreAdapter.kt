package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.themovieapp.data.remote.model.Genre
import com.vishalpvijayan.themovieapp.databinding.ItemGenreBinding

class GenreAdapter(
    private val onClick: (Genre) -> Unit
) : ListAdapter<Genre, GenreAdapter.GenreHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Genre>() {
        override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean = oldItem == newItem
    }

    inner class GenreHolder(private val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Genre) {
            binding.tvGenreName.text = item.name
            binding.ivGenreIcon.setImageResource(GenreIconMapper.iconFor(item.name))
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreHolder {
        val binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreHolder, position: Int) = holder.bind(getItem(position))
}
