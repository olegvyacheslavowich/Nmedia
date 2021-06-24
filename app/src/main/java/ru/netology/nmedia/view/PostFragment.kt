package ru.netology.nmedia.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentPostChangeBinding
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.util.Util
import ru.netology.nmedia.view.PostsFragment.Companion.newPostArg
import ru.netology.nmedia.view.PostsFragment.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment,
        factoryProducer = {
            DependencyContainer.getInstance(requireContext().applicationContext).viewModelFactory
        })
    private var fragmentBinding: FragmentPostChangeBinding? = null
    private var attachFabClicked = false

    private val photoRequestCode = 1
    private val cameraRequestCode = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostChangeBinding.inflate(layoutInflater)
        fragmentBinding = binding
        setContent(binding.postTextEditText)

        with(binding) {
            attachFab.setOnClickListener {
                attachFabClicked = !attachFabClicked
                setVisibilityAttachFab(this, attachFabClicked)
            }

            pickPhoto.setOnClickListener {
                ImagePicker.with(this@PostFragment)
                    .crop()
                    .compress(2048)
                    .galleryOnly()
                    .galleryMimeTypes(
                        arrayOf("image/png", "image/jpeg")
                    )
                    .start(photoRequestCode)
            }

            takePhoto.setOnClickListener {
                ImagePicker.with(this@PostFragment)
                    .crop()
                    .compress(2048)
                    .cameraOnly()
                    .start(cameraRequestCode)
            }

            deletePhoto.setOnClickListener {
                viewModel.changePhoto(null, null)
            }
        }

        viewModel.photo.observe(viewLifecycleOwner) { photoModel ->
            if (photoModel.uri == null) {
                binding.postImage.visibility = View.GONE
                binding.deletePhoto.visibility = View.GONE
                return@observe
            }
            binding.postImage.setImageURI(photoModel.uri)
            binding.postImage.visibility = View.VISIBLE
            binding.deletePhoto.visibility = View.VISIBLE
        }


        val isNewPost = arguments?.newPostArg == true

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.draftContent.observe(viewLifecycleOwner) {
            if (isNewPost) {
                binding.postTextEditText.setText(it)
            }
        }

        viewModel.postState.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_saving, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry_action_title) {
                        viewModel.save()
                    }
                    .show()
                return@observe
            }

            if (state.saved) {
                viewModel.deleteDraftContent()
                findNavController().navigateUp()
            }
        }

        binding.postTextEditText.requestFocus()


        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (isNewPost) {
                val content = binding.postTextEditText.text.toString()
                viewModel.saveDraftContent(content)
            }
            findNavController().navigateUp()
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_post_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save -> {
                fragmentBinding?.let { binding ->
                    viewModel.changeContent(binding.postTextEditText.text.toString())
                    viewModel.save()
                    Util.hideKeyboard(binding.root)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == ImagePicker.RESULT_ERROR) {
            fragmentBinding?.let {
                Snackbar.make(it.root, ImagePicker.getError(data), Snackbar.LENGTH_SHORT)
                    .show()
            }
            return
        }

        if (resultCode == Activity.RESULT_OK && (requestCode == cameraRequestCode || requestCode == photoRequestCode)) {
            val uri = data?.data
            val file = uri?.toFile()
            viewModel.changePhoto(uri, file)
        }


    }

    private fun setContent(contentEditText: EditText) {
        arguments?.textArg.let {
            contentEditText.setText(it)
        }
    }

    private fun setVisibilityAttachFab(binding: FragmentPostChangeBinding, clicked: Boolean) {
        if (clicked) {
            binding.takePhoto.show()
            binding.pickPhoto.show()
            binding.attachFab.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_baseline_close_24,
                    context?.theme
                )
            )
        } else {
            binding.takePhoto.hide()
            binding.pickPhoto.hide()
            binding.attachFab.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_baseline_attach_file_24,
                    context?.theme
                )
            )
        }
    }


}