package com.vishalpvijayan.themovieapp.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vishalpvijayan.themovieapp.domain.model.User
import com.vishalpvijayan.themovieapp.utilis.toUser

class UserPagingSource(
    private val remoteDataSource: UserRemoteDataSource
) : PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val response = remoteDataSource.getUsers(page)
            LoadResult.Page(
                data = response.data.map { it.toUser() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = null
}



