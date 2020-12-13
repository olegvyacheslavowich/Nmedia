package ru.netology.nmedia.view

import android.os.Bundle
import android.text.TextUtils
import android.view.ContextMenu
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostsBinding
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.util.Util
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

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
        })

        viewModel.dataList.observe(this) {
            postAdapter.submitList(it)
        }

        val adAdapter = AdAdapter()
        viewModel.adData.observe(this) {
            adAdapter.submitList(it)
        }

        val adapters = listOf(adAdapter, postAdapter)
        val mergeAdapter = ConcatAdapter(adapters)

        binding.apply {
            postsRecyclerView.adapter = mergeAdapter

            sendImageView.setOnClickListener {
                with(currentPostContentEditText) {
                    if (TextUtils.isEmpty(text)) {
                        return@setOnClickListener
                    }

                    viewModel.changeContent(currentPostContentEditText.text.toString())
                    viewModel.save()

                    setText("")
                    clearFocus()
                    Util.hideKeyboard(this)
                }
            }

            cancelImageView.setOnClickListener {
                with(currentPostContentEditText) {
                    setText("")
                    clearFocus()
                    Util.hideKeyboard(this)
                }
            }
        }


        viewModel.edited.observe(this) { post ->
            if (post.id == 0) {
                return@observe
            }

            with(binding.currentPostContentEditText) {
                requestFocus()
                setText(post.content)
            }

        }

    }
}