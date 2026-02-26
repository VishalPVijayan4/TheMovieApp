package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.databinding.ItemMovieGridBinding

class MovieGridAdapter(
    private val onItemClick: (Movie, ImageView) -> Unit
) : ListAdapter<Movie, MovieGridAdapter.MovieViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }

    inner class MovieViewHolder(private val binding: ItemMovieGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.titleTextView.text = item.title ?: "Untitled"
            binding.ratingTextView.text = "⭐ ${item.vote_average?.let { String.format("%.1f", it) } ?: "N/A"}"
            val transitionName = "poster_${item.id}"
            ViewCompat.setTransitionName(binding.posterImageView, transitionName)
            loadImage(item)
            binding.root.setOnClickListener { onItemClick(item, binding.posterImageView) }
            binding.btnImageRetry.setOnClickListener { loadImage(item) }
        }

        private fun loadImage(item: Movie) {
            val posterPath = item.poster_path
            if (posterPath.isNullOrBlank()) {
                binding.posterImageView.setImageResource(R.drawable.ic_movies)
                binding.btnImageRetry.isVisible = true
                return
            }
            binding.btnImageRetry.isVisible = false
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w500$posterPath")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        binding.btnImageRetry.isVisible = true
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        binding.btnImageRetry.isVisible = false
                        return false
                    }
                })
                .into(binding.posterImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder.bind(getItem(position))
}
