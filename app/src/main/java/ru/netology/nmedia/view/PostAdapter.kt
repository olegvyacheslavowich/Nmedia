package ru.netology.nmedia.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.BuildConfig
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

            authorImageView.loadImg("${BuildConfig.BASE_URL}/avatars/", post.authorAvatar)
            authorTextView.text = post.author
            publishedTextView.text = post.published.timeToString()
            contentTextView.text = post.content
            viewsButton.text = Util.parseNumber(post.viewCount)
            likesButton.isChecked = post.liked
            likesButton.text = Util.parseNumber(post.likesCount)
            shareButton.text = Util.parseNumber(post.shareCount)
            notSaved.visibility = View.INVISIBLE
            postMenuImageView.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            attachmentImageView.loadImg(
                "${BuildConfig.BASE_URL}/media/",
                post.attachment?.url ?: ""
            )

            attachmentImageView.setOnClickListener {
                onInteractionListener.onPlay(post)
            }

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