package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.data.remote.model.Movie
import com.vishalpvijayan.themovieapp.databinding.ItemBannerBinding

class BannerAdapter(
    private val onItemClick: (Movie, ImageView) -> Unit
) : ListAdapter<Movie, BannerAdapter.BannerViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
    }

    inner class BannerViewHolder(private val binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.tvBannerTitle.text = item.title ?: "Untitled"
            val image = item.backdrop_path ?: item.poster_path
            ViewCompat.setTransitionName(binding.ivBanner, "poster_${item.id}")
            Glide.with(binding.root).load("https://image.tmdb.org/t/p/w780$image").into(binding.ivBanner)
            binding.root.setOnClickListener { onItemClick(item, binding.ivBanner) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) = holder.bind(getItem(position))
}
