package ru.netology.nmedia.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import ru.netology.nmedia.databinding.ActivityPostsBinding
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class PostsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val postAdapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
        })

        viewModel.dataList.observe(this) {
            postAdapter.submitList(it)
        }

        val adAdapter = AdAdapter()
        viewModel.adData.observe(this){
            adAdapter.submitList(it)
        }

        val adapters = listOf(adAdapter, postAdapter)
        val mergeAdapter = ConcatAdapter(adapters)

        binding.postsRecyclerView.adapter = mergeAdapter
    }
}