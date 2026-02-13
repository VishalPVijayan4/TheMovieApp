package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.databinding.ItemMovieGridBinding

class MovieGridAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieGridAdapter.MovieViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }

    inner class MovieViewHolder(private val binding: ItemMovieGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.titleTextView.text = item.title ?: "Untitled"
            binding.ratingTextView.text = "⭐ ${item.vote_average?.let { String.format("%.1f", it) } ?: "N/A"}"
            Glide.with(binding.root).load("https://image.tmdb.org/t/p/w500${item.poster_path}").into(binding.posterImageView)
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(getItem(position))
}
