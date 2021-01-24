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
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.util.Util
import ru.netology.nmedia.view.PostsFragment.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostCardFragment : Fragment() {

    companion object {
        private const val intentType = "text/plain"
    }

    private val viewModel: PostViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = CardPostBinding.inflate(layoutInflater)

        viewModel.postData.observe(viewLifecycleOwner, { post ->
            binding.apply {
                authorTextView.text = post.author
                publishedTextView.text = post.published
                contentTextView.text = post.content
                viewsButton.text = Util.parseNumber(post.viewCount)
                likesButton.isChecked = post.liked
                likesButton.text = Util.parseNumber(post.likesCount)
                shareButton.text = Util.parseNumber(post.shareCount)

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
                        viewModel.shareById(post.id)
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

                playVideoImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
                        startActivity(intent)
                    }
                }

                videoImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
                        startActivity(intent)
                    }
                }
            }
        })

        return binding.root

    }
}