package ru.netology.nmedia.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentRegistrationBinding
import ru.netology.nmedia.viewmodel.RegistrationViewModel


class RegistrationFragment : Fragment() {


    private val viewModel: RegistrationViewModel by viewModels(::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentRegistrationBinding.inflate(inflater)

        viewModel.data.observe(viewLifecycleOwner) { state ->
            if (state == null || state.id == 0 || state.token == "") {
                return@observe
            }
            findNavController().navigateUp()
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                return@observe
            }
            binding.progressIndicator.isVisible = state.loading
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry_action_title) {
                        register(binding)
                    }
                    .show()
            }
        }

        with(binding) {
            registerButton.setOnClickListener {
                register(this)
            }
        }

        return binding.root
    }

    private fun register(binding: FragmentRegistrationBinding) {

        var validated = true

        with(binding) {
            val name = nameEditText.text.toString()
            val login = loginEditText.text.toString()
            val pass = passwordEditText.text.toString()
            val retryPass = retryPasswordEditText.text.toString()

            retryPasswordTextInput.error = null
            nameTextInput.error = null
            loginTextInput.error = null
            passwordTextInput.error = null

            if (name.isEmpty()) {
                nameTextInput.error = getString(R.string.empty_name)
                validated = false
            }

            if (login.isEmpty()) {
                loginTextInput.error = getString(R.string.empty_login)
                validated = false
            }

            if (pass.isEmpty()) {
                passwordTextInput.error = getString(R.string.empty_pass)
                validated = false
            }

            if (retryPass.isEmpty()) {
                retryPasswordTextInput.error = getString(R.string.empty_pass)
                validated = false
            }

            if (retryPass != pass) {
                retryPasswordTextInput.error = getString(R.string.password_are_different)
                validated = false
            }

            if (validated) {
                viewModel.register(name, login, pass)
            }
        }
    }
}