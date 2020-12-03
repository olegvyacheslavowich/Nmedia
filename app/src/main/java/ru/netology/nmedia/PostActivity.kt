package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.util.Util

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            0,
            "Natali Karpenko",
            "02 december 2020",
            "Android is the most popular mobile platform, so there is a shortage of Android developers: more than 500 vacancies appear every month (according to hh.ru). \\n\\nAndroid developers are needed in different areas: to make online banking with a complex degree of protection or an application for search for a soul mate, develop applications for learning English or a mobile service for finding flights.",
            false,
            10_299_999,
            false,
            0,
            44
        )

        with(binding) {
            authorTextView.text = post.author
            publishedTextView.text = post.published
            contentTextView.text = post.content
            likesTextView.text = Util.parseNumber(post.likesCount)
            shareTextView.text = Util.parseNumber(post.shareCount)
            viewsTextView.text = Util.parseNumber(post.viewCount)
            if (post.liked) {
                likesImageView.setImageResource(R.drawable.ic_baseline_thumb_up_24)
            }

            likesImageView.setOnClickListener {

                post = post.copy(
                    liked = !post.liked,
                    likesCount = if (!post.liked) post.likesCount + 1 else post.likesCount - 1
                )

                likesImageView.setImageResource(
                    if (post.liked) R.drawable.ic_baseline_thumb_up_24 else R.drawable.ic_thumb_up_black_24dp
                )
                likesTextView.text = Util.parseNumber(post.likesCount)

            }

            shareImageView.setOnClickListener {

                post = post.copy(shareCount = post.shareCount + 1)
                shareTextView.text = Util.parseNumber(post.shareCount)

            }
        }


    }
}
