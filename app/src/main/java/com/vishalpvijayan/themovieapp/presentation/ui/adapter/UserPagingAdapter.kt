package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vishalpvijayan.themovieapp.R
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.utilis.AppGlobals
import com.vishalpvijayan.themovieapp.utilis.OnItemClickListener


class UserPagingAdapter(private val onItemClickListener: OnItemClickListener) : PagingDataAdapter<User, UserPagingAdapter.UserViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false),
        onItemClickListener
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        user?.let { holder.bind(it) }
    }

    class UserViewHolder(view: View, private val onItemClickListener: OnItemClickListener) :
        RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            itemView.findViewById<TextView>(R.id.firstNameText).text = user.fullName
            itemView.findViewById<TextView>(R.id.lastNameText).text = user.position
            itemView.setOnClickListener {
                AppGlobals.globalStringbaseUrl = "https://api.themoviedb.org/3/"
                onItemClickListener.onItemClick(adapterPosition)
            }

                Glide.with(itemView.context)
                    .load(user.profilePic)
                    .into(itemView.findViewById(R.id.avatarImage))
            }
        }

        object DiffCallback : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
        }
    }

