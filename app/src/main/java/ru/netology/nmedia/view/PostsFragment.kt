package ru.netology.nmedia.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostsBinding
import ru.netology.nmedia.model.post.Post
import ru.netology.nmedia.util.NewPostArg
import ru.netology.nmedia.util.PostArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostsFragment : Fragment() {

    companion object {
        const val intentType = "text/plain"
        var Bundle.textArg: String? by StringArg
        var Bundle.newPostArg: Boolean by NewPostArg
        var Bundle.postArg: Post? by PostArg
    }

    private val viewModel: PostViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostsBinding.inflate(inflater, container, false)
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true;
            viewModel.loadPosts()
            binding.swipeRefresh.isRefreshing = false;
        }

        val postAdapter = PostAdapter(object : OnInteractionListener {
            override fun onClicked(post: Post) {
                findNavController().navigate(
                    R.id.action_postsFragment_to_postCardFragment,
                    Bundle().apply {
                        postArg = post
                    })
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
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("post.videoUrl"))
                if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
                    startActivity(intent)
                }
            }
        })

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state.loading
            binding.swipeRefresh.isRefreshing = state.refreshing
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry_action_title) {
                        viewModel.loadPosts()
                    }
                    .show()
            }

        }
        viewModel.data.observe(viewLifecycleOwner) { state ->
            postAdapter.submitList(state.posts)
        }

        binding.apply {
            viewModel.newCount.observe(viewLifecycleOwner) { count ->
                if (count > 0) binding.updatePostsButton.show()
            }

            updatePostsButton.setOnClickListener {
                viewModel.updateShowing()
                updatePostsButton.hide()
            }

            swipeRefresh.setOnRefreshListener {
                viewModel.refreshPosts()
            }
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