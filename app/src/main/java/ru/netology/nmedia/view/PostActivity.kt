package ru.netology.nmedia.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.util.Util
import ru.netology.nmedia.viewmodel.PostViewModel

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this, { post ->
            with(binding) {
                authorTextView.text = post.author
                publishedTextView.text = post.published
                contentTextView.text = post.content
                likesTextView.text = Util.parseNumber(post.likesCount)
                shareTextView.text = Util.parseNumber(post.shareCount)
                viewsTextView.text = Util.parseNumber(post.viewCount)
                if (post.liked) likesImageView.setImageResource(R.drawable.ic_baseline_thumb_up_24)
                else likesImageView.setImageResource(R.drawable.ic_thumb_up_black_24dp)
            }
        })

        binding.likesImageView.setOnClickListener {
            viewModel.like()
        }

        binding.shareImageView.setOnClickListener {
            viewModel.share()
        }



    }
}
