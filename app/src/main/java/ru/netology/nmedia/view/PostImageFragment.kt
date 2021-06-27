package ru.netology.nmedia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.databinding.FragmentPostImageBinding
import ru.netology.nmedia.util.loadImg
import ru.netology.nmedia.view.PostsFragment.Companion.textArg

@AndroidEntryPoint
class PostImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostImageBinding.inflate(inflater, container, false)

        arguments?.textArg.let {
            binding.postImageView.loadImg("${BuildConfig.BASE_URL}/media/", it.orEmpty())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        return binding.root

    }
}