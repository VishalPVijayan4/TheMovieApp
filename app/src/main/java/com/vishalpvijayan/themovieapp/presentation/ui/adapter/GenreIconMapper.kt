package com.vishalpvijayan.themovieapp.presentation.ui.adapter

import com.vishalpvijayan.themovieapp.R

object GenreIconMapper {
    fun iconFor(name: String): Int {
        val value = name.lowercase()
        return when {
            "action" in value || "war" in value -> R.drawable.ic_genre_action
            "adventure" in value || "western" in value -> R.drawable.ic_genre_adventure
            "animation" in value || "family" in value -> R.drawable.ic_genre_family
            "comedy" in value -> R.drawable.ic_genre_comedy
            "drama" in value || "romance" in value -> R.drawable.ic_genre_drama
            "horror" in value || "thriller" in value || "mystery" in value -> R.drawable.ic_genre_horror
            "science" in value || "sci" in value || "fantasy" in value -> R.drawable.ic_genre_scifi
            "documentary" in value || "history" in value -> R.drawable.ic_genre_documentary
            else -> R.drawable.ic_genre_default
        }
    }
}
