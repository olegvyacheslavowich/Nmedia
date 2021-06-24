package ru.netology.nmedia.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentAuthorizationBinding
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.viewmodel.AuthorizationViewModel

class AuthorizationFragment : Fragment() {

    val viewModel: AuthorizationViewModel by viewModels(
        ownerProducer = ::requireParentFragment,
        factoryProducer = {
            DependencyContainer.getInstance(requireContext().applicationContext).viewModelFactory
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAuthorizationBinding.inflate(inflater)

        viewModel.data.observe(viewLifecycleOwner) { state ->
            if (state == null || state.id == 0 || state.token == "") {
                return@observe
            }
            findNavController().navigate(R.id.action_authorizationFragment_to_postsFragment)
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                return@observe
            }
            binding.progressIndicator.isVisible = state.loading
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry_action_title) {
                        viewModel.login(
                            binding.loginEditText.text.toString(),
                            binding.passwordEditText.text.toString()
                        )
                    }
                    .show()
            }
        }

        with(binding) {
            loginButton.setOnClickListener {
                viewModel.login(loginEditText.text.toString(), passwordEditText.text.toString())
            }
            signUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
            }
        }

        return binding.root
    }
}