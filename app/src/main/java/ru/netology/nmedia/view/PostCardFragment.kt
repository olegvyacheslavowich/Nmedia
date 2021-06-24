package ru.netology.nmedia.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.model.post.getEmptyPost
import ru.netology.nmedia.util.Util
import ru.netology.nmedia.util.Util.timeToString
import ru.netology.nmedia.util.loadImg
import ru.netology.nmedia.view.PostsFragment.Companion.newPostArg
import ru.netology.nmedia.view.PostsFragment.Companion.postArg
import ru.netology.nmedia.view.PostsFragment.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {

    companion object {
        private const val intentType = "text/plain"
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment,
        factoryProducer = {
            DependencyContainer.getInstance(requireContext().applicationContext).viewModelFactory
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardPostBinding.inflate(layoutInflater)

        val post = arguments?.postArg ?: getEmptyPost()
        binding.apply {
            authorImageView.loadImg("${BuildConfig.BASE_URL}/avatars/", post.authorAvatar)
            authorTextView.text = post.author
            publishedTextView.text = post.published.timeToString()
            contentTextView.text = post.content
            viewsButton.text = Util.parseNumber(post.viewCount)
            likesButton.isChecked = post.liked
            likesButton.text = Util.parseNumber(post.likesCount)
            shareButton.text = Util.parseNumber(post.shareCount)
            postMenuImageView.visibility = if (post.ownedByMe) View.VISIBLE else View.INVISIBLE

            attachmentImageView.loadImg(
                "${BuildConfig.BASE_URL}/media/",
                post.attachment?.url ?: ""
            )

            likesButton.setOnClickListener {
                viewModel.likeById(post.id)
            }

            shareButton.setOnClickListener {
                val intent = Intent()
                    .setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, post.content)
                    .setType(intentType)
                val shareIntent = Intent.createChooser(intent, "Post content")

                if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
                    startActivity(shareIntent)
                }
            }

            postMenuImageView.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.remove -> {
                                viewModel.removeById(post.id)
                                findNavController().navigateUp()
                                true
                            }
                            R.id.edit -> {
                                viewModel.edit(post)
                                findNavController().navigate(R.id.action_postCardFragment_to_postFragment,
                                    Bundle().apply {
                                        textArg = post.content
                                    })
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            attachmentImageView.setOnClickListener {
                findNavController().navigate(R.id.action_postCardFragment_to_postImageFragment,
                    Bundle().apply {
                        textArg = post.attachment?.url
                    })
            }
        }

        return binding.root

    }
}