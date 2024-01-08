package com.solodilov.coffeeshopapp.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.solodilov.coffeeshopapp.presentation.common.UiState
import com.solodilov.coffeeshopapp.presentation.common.ViewModelFactory
import com.solodilov.coffeeshopapp.presentation.common.collectFlow
import com.solodilov.coffeeshopapp.presentation.common.onError
import com.solodilov.coffeeshopapp.presentation.common.onSuccess
import com.solodilov.coffeeshopapp.presentation.common.viewBinding
import com.solodilov.coffeeshopapp.App
import com.solodilov.coffeeshopapp.R
import com.solodilov.coffeeshopapp.databinding.FragmentLoginBinding
import com.solodilov.coffeeshopapp.presentation.common.*
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectFlow(viewModel.uiState, ::handleState)
        collectFlow(viewModel.emailError, ::handleLoginField)
        collectFlow(viewModel.passwordError, ::handlePasswordField)
    }

    private fun initViews() {
        binding.email.addTextChangedListener { text ->
            viewModel.onEmailTextChanged(text?.toString() ?: "")
        }

        binding.password.addTextChangedListener { text ->
            viewModel.onPasswordTextChanged(text?.toString() ?: "")
        }

        binding.enterButton.setOnClickListener { viewModel.onLoginClicked() }

        binding.noAccount.setOnClickListener { startRegisterFragment() }
    }

    private fun handleState(state: UiState<Boolean?>) = with(binding) {
        progressBar.isVisible = state is UiState.Loading
        loginLayout.isVisible = state !is UiState.Loading

        state
            .onSuccess { data ->
                when (data) {
                    true -> startCafeListFragment()
                    false -> showToast(getString(R.string.error))
                    else -> {}
                }
            }
            .onError { error -> showToast(error.message.toString()) }
    }

    private fun handleLoginField(hasError: Boolean) = with(binding) {
        emailTextInput.error = if (hasError) getString(R.string.empty_text_field) else null
    }

    private fun handlePasswordField(hasError: Boolean) = with(binding) {
        passwordTextInput.error = if (hasError) getString(R.string.empty_text_field) else null
    }

    private fun startCafeListFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_cafeListFragment)
        viewModel.clearState()
    }

    private fun startRegisterFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        viewModel.clearState()
    }
}