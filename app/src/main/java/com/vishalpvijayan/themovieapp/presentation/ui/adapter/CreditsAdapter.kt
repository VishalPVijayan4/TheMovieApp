package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.data.remote.model.CreditPerson
import com.vishalpvijayan.themovieapp.databinding.ItemCreditBinding

class CreditsAdapter(
    private val onItemClick: ((CreditPerson) -> Unit)? = null
) : ListAdapter<CreditPerson, CreditsAdapter.CreditViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<CreditPerson>() {
        override fun areItemsTheSame(oldItem: CreditPerson, newItem: CreditPerson) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: CreditPerson, newItem: CreditPerson) = oldItem == newItem
    }

    inner class CreditViewHolder(private val binding: ItemCreditBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CreditPerson) {
            binding.tvName.text = item.name ?: "Unknown"
            binding.tvCharacter.text = item.character ?: ""
            val image = item.profile_path?.let { "https://image.tmdb.org/t/p/w342$it" }
            Glide.with(binding.root).load(image).into(binding.ivProfile)
            binding.root.setOnClickListener { onItemClick?.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val binding = ItemCreditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) = holder.bind(getItem(position))
}
