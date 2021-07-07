package ru.netology.nmedia.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardAdBinding
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.model.AdModel
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.PostModel
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
    PagingDataAdapter<FeedModel, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is AdModel -> R.layout.card_ad
            is PostModel -> R.layout.card_post
            else -> error("Unknown type of ${position}")

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.card_post -> {
                val binding =
                    CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PostViewHolder(binding, onInteractionListener)
            }
            R.layout.card_ad -> {
                val binding =
                    CardAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AdInPostsViewHolder(binding)
            }
            else -> error("Unknown view type ${viewType}")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> {
                val item = getItem(position) as PostModel
                holder.bind(item.post)
            }
            is AdInPostsViewHolder -> {
                val item = getItem(position) as AdModel
                holder.bind(item)
            }

        }
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

class AdInPostsViewHolder(
    private val binding: CardAdBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(adModel: AdModel) {
        binding.apply {
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<FeedModel>() {
    override fun areItemsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean {
        if (oldItem.javaClass != newItem.javaClass)
            return false

        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeedModel, newItem: FeedModel): Boolean {
        return oldItem == newItem
    }
}