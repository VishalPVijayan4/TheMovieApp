package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.databinding.ItemProfileFavoriteBinding

class ProfileFavoriteAdapter(
    private val onItemClick: (Movie, ImageView) -> Unit
) : ListAdapter<Movie, ProfileFavoriteAdapter.FavoriteViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }

    inner class FavoriteViewHolder(private val binding: ItemProfileFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.tvTitle.text = item.title ?: "Untitled"
            ViewCompat.setTransitionName(binding.ivPoster, "poster_${item.id}")
            Glide.with(binding.root)
                .load("https://image.tmdb.org/t/p/w342${item.poster_path ?: item.backdrop_path}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(binding.ivPoster)
            binding.root.setOnClickListener { onItemClick(item, binding.ivPoster) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemProfileFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) = holder.bind(getItem(position))
}
