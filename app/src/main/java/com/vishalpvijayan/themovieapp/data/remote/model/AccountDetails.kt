package com.vishalpvijayan.themovieapp.data.remote.model

data class AccountDetails(
    val id: Int,
    val name: String? = null,
    val username: String? = null,
    val avatar: AvatarWrapper? = null
)

data class AvatarWrapper(
    val gravatar: GravatarAvatar? = null,
    val tmdb: TmdbAvatar? = null
)

data class GravatarAvatar(
    val hash: String? = null
)

data class TmdbAvatar(
    val avatar_path: String? = null
)
