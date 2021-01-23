package ru.netology.nmedia.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsBinding
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.util.NewPostArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostsFragment : Fragment() {

    companion object {
        const val intentType = "text/plain"
        var Bundle.textArg: String? by StringArg
        var Bundle.newPostArg: Boolean by NewPostArg
    }

    private val viewModel: PostViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsBinding.inflate(inflater, container, false)

        val postAdapter = PostAdapter(object : OnInteractionListener {
            override fun onClicked(post: Post) {
                viewModel.setPostData(post)
                findNavController().navigate(R.id.action_postsFragment_to_postCardFragment)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
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

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)

                findNavController().navigate(R.id.action_postsFragment_to_postFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
            }

            override fun onPlay(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
                    startActivity(intent)
                }
            }
        })

        viewModel.dataList.observe(viewLifecycleOwner) {
            postAdapter.submitList(it)
        }

        val adAdapter = AdAdapter()
        viewModel.adData.observe(viewLifecycleOwner) {
            adAdapter.submitList(it)
        }

        val adapters = listOf(adAdapter, postAdapter)
        val mergeAdapter = ConcatAdapter(adapters)

        binding.apply {
            postsRecyclerView.adapter = mergeAdapter

            postAddFab.setOnClickListener {
                findNavController().navigate(R.id.action_postsFragment_to_postFragment,
                    Bundle().apply {
                        newPostArg = true
                    })
            }
        }

        return binding.root
    }
}