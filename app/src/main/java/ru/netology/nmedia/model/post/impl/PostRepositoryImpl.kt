package ru.netology.nmedia.model.post.impl

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.posts.PostsApi
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.model.post.PostRepository
import ru.netology.nmedia.model.post.getEmptyPost

class PostRepositoryImpl : PostRepository {

    override fun getAll(callback: PostRepository.GetAllCallback) {

        PostsApi.retrofitService.getAll()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: emptyList())
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun getById(id: Int, callback: PostRepository.GetByIdCallback) {

        PostsApi.retrofitService
            .getById(id)
            .enqueue(object : Callback<Post> {
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess(response.body() ?: getEmptyPost())
                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun likeById(id: Int, callback: PostRepository.GeneralCallback) {

        PostsApi.retrofitService
            .likeById(id)
            .enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }
            })

    }

    override fun shareById(id: Int) {
    }

    override fun removeById(id: Int, callback: PostRepository.GeneralCallback) {

        PostsApi.retrofitService
            .removeById(id)
            .enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun save(post: Post, callback: PostRepository.GeneralCallback) {

        PostsApi.retrofitService
            .save(post)
            .enqueue(object: Callback<Any>{
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }

                    callback.onSuccess()
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

}
