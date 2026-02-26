package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.databinding.ItemMovieBinding

class MovieAdapter(
    private val onItemClick: (Movie, ImageView) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.titleTextView.text = movie.title ?: "Untitled"
            binding.ratingTextView.text = "⭐ ${movie.vote_average?.let { String.format("%.1f", it) } ?: "N/A"}"
            val transitionName = "poster_${movie.id}"
            ViewCompat.setTransitionName(binding.posterImageView, transitionName)

            loadPoster(movie)

            binding.root.setOnClickListener {
                onItemClick(movie, binding.posterImageView)
            }
            binding.btnImageRetry.setOnClickListener { loadPoster(movie) }
        }

        private fun loadPoster(movie: Movie) {
            val posterPath = movie.poster_path
            if (posterPath.isNullOrBlank()) {
                binding.posterImageView.setImageResource(com.vishalpvijayan.themovieapp.R.drawable.ic_movies)
                binding.btnImageRetry.isVisible = true
                return
            }
            binding.btnImageRetry.isVisible = false
            val posterUrl = "https://image.tmdb.org/t/p/w500$posterPath"
            Glide.with(binding.posterImageView.context)
                .load(posterUrl)
                .listener(object : RequestListener<android.graphics.drawable.Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<android.graphics.drawable.Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.btnImageRetry.isVisible = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: android.graphics.drawable.Drawable,
                        model: Any,
                        target: Target<android.graphics.drawable.Drawable>?,
                        dataSource: com.bumptech.glide.load.DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.btnImageRetry.isVisible = false
                        return false
                    }
                })
                .into(binding.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}
