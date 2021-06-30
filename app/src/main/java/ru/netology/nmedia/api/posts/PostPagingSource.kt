package ru.netology.nmedia.api.posts

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.model.post.Post

class PostPagingSource(private val apiService: PostsApiService) :
    PagingSource<Int, Post>() {
    override fun getRefreshKey(state: PagingState<Int, Post>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {

        try {
            val result = when (params) {
                is LoadParams.Refresh -> apiService.getLatest(params.loadSize)
                is LoadParams.Append -> apiService.getBefore(params.key, params.loadSize)
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(),
                    prevKey = params.key,
                    nextKey = null
                )
            }

            val data = result.body() ?: error("Body is empty")
            val lastKey = data.lastOrNull()?.id

            return LoadResult.Page(data = data, prevKey = params.key, nextKey = lastKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}