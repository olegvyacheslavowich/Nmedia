package ru.netology.nmedia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentPostChangeBinding
import ru.netology.nmedia.util.Util
import ru.netology.nmedia.view.PostsFragment.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostChangeBinding.inflate(layoutInflater)
        setContent(binding.postTextEditText)

        binding.postTextEditText.requestFocus()
        binding.addPostFab.setOnClickListener {
            if (!binding.postTextEditText.text.isNullOrBlank()) {
                viewModel.changeContent(binding.postTextEditText.text.toString())
                viewModel.save()
            }
            Util.hideKeyboard(binding.root)
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun setContent(contentEditText: EditText) {
        arguments?.textArg.let {
            contentEditText.setText(it)
        }
    }


}