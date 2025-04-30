package com.vishalpvijayan.themovieapp.presentation.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vishalpvijayan.themovieapp.data.local.entity.UserEntity
import com.vishalpvijayan.themovieapp.databinding.ItemOfflineUserBinding

class OfflineUserAdapter(
    private val onSyncClicked: (UserEntity) -> Unit
) : ListAdapter<UserEntity, OfflineUserAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemOfflineUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserEntity) {
            binding.textViewName.text = user.name
            binding.textViewPosition.text = user.job
            binding.buttonSync.setOnClickListener {
                onSyncClicked(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOfflineUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean = oldItem.userId == newItem.userId
        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean = oldItem == newItem
    }
}
