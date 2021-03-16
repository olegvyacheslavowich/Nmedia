package ru.netology.nmedia.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.util.Util
import ru.netology.nmedia.util.Util.timeToString
import ru.netology.nmedia.util.loadImg

interface OnInteractionListener {
    fun onClicked(post: Post)
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onPlay(post: Post)
}

class PostAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {

            authorImageView.loadImg("http://85.115.173.83:9194/avatars/", post.authorAvatar)
            authorTextView.text = post.author
            publishedTextView.text = post.published.timeToString()
            contentTextView.text = post.content
            viewsButton.text = Util.parseNumber(post.viewCount)
            likesButton.isChecked = post.liked
            likesButton.text = Util.parseNumber(post.likesCount)
            shareButton.text = Util.parseNumber(post.shareCount)
            attachmentImageView.loadImg(
                "http://85.115.173.83:9194/images/",
                post.attachment?.url ?: ""
            )

            root.setOnClickListener {
                onInteractionListener.onClicked(post)
            }

            likesButton.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shareButton.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            postMenuImageView.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            playVideoImageView.setOnClickListener {
                onInteractionListener.onPlay(post)
            }

            attachmentImageView.setOnClickListener {
                onInteractionListener.onPlay(post)
            }


        }
    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}