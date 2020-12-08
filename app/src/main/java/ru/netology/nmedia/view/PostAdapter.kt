package ru.netology.nmedia.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityPostBinding
import ru.netology.nmedia.model.Post
import ru.netology.nmedia.util.Util

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
}

class PostAdapter(private val onInteractionListener: OnInteractionListener) :
    RecyclerView.Adapter<PostViewHolder>() {

    var posts = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ActivityPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}


class PostViewHolder(
    private val binding: ActivityPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            authorTextView.text = post.author
            publishedTextView.text = post.published
            contentTextView.text = post.content
            likesTextView.text = Util.parseNumber(post.likesCount)
            shareTextView.text = Util.parseNumber(post.shareCount)
            viewsTextView.text = Util.parseNumber(post.viewCount)
            if (post.liked) likesImageView.setImageResource(R.drawable.ic_baseline_thumb_up_24)
            else likesImageView.setImageResource(R.drawable.ic_thumb_up_black_24dp)

            likesImageView.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shareImageView.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }

}