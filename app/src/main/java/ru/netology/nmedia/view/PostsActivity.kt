package ru.netology.nmedia.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import ru.netology.nmedia.databinding.ActivityPostsBinding
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class PostsActivity : AppCompatActivity() {

    private val requestCodeChangePost = 1
    private val intentType = "text/plain"

    private val viewModel: PostViewModel by viewModels()

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
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, post.content)
                    .setType(intentType)
                val shareIntent = Intent.createChooser(intent, "Post content")

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(shareIntent)
                    viewModel.shareById(post.id)
                }
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                val intent = Intent(this@PostsActivity, PostActivity::class.java)
                intent.putExtra(Intent.EXTRA_TEXT, post.content)
                startActivityForResult(intent, requestCodeChangePost)
            }

            override fun onPlay(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
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

            postAddFab.setOnClickListener {
                val intent = Intent(this@PostsActivity, PostActivity::class.java)
                startActivityForResult(intent, requestCodeChangePost)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            requestCodeChangePost -> {
                if (resultCode != Activity.RESULT_OK) {
                    return
                }

                data?.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    viewModel.changeContent(it)
                    viewModel.save()
                }
            }
        }

    }
}