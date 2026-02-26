package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vishalpvijayan.themovieapp.data.remote.model.SearchResultItem
import com.vishalpvijayan.themovieapp.databinding.ItemSearchResultBinding

class SearchAdapter(
    private val onItemClick: (SearchResultItem) -> Unit
) : ListAdapter<SearchResultItem, SearchAdapter.SearchViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<SearchResultItem>() {
        override fun areItemsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean =
            oldItem.id == newItem.id && oldItem.mediaType == newItem.mediaType

        override fun areContentsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean = oldItem == newItem
    }

    inner class SearchViewHolder(private val binding: ItemSearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResultItem) {
            binding.tvTitle.text = item.title ?: "Untitled"
            binding.tvMeta.text = when (item.mediaType) {
                "person" -> {
                    val knownMovies = item.knownFor.orEmpty().take(3).mapNotNull { it.title }.joinToString()
                    if (knownMovies.isBlank()) "Person" else "Person • Known for: $knownMovies"
                }
                else -> "${item.mediaType.uppercase()} • ⭐ ${item.voteAverage?.let { String.format("%.1f", it) } ?: "N/A"}"
            }
            Glide.with(binding.root).load("https://image.tmdb.org/t/p/w342${item.imagePath}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate().into(binding.ivPoster)
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) = holder.bind(getItem(position))
}
