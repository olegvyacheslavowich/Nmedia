package ru.netology.nmedia.api.posts

import androidx.paging.*
import ru.netology.nmedia.db.dao.post.PostDao
import ru.netology.nmedia.db.entity.PostEntity
import ru.netology.nmedia.db.entity.toDto
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.toEntity

@ExperimentalPagingApi
class PostRemoteMediator(
    private val apiService: PostsApiService,
    private val dao: PostDao
) :
    RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {

            val loadSize = state.config.pageSize
            val result = when (loadType) {
                LoadType.REFRESH -> {
                    val item = state.lastItemOrNull() ?: return MediatorResult.Success(false)
                    val data = apiService.getLatest(loadSize).body() ?: error("Body is empty")
                    data.takeLastWhile { post ->
                        post.id > item.id
                    }
                }
                LoadType.APPEND -> {
                    val item = state.lastItemOrNull() ?: return MediatorResult.Success(false)
                    apiService.getLatest(loadSize).body() ?: error("Body is empty")
                }
                LoadType.PREPEND -> return MediatorResult.Success(false)
            }

            dao.insert(result.map(Post::toEntity))
            return MediatorResult.Success(result.isNotEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}