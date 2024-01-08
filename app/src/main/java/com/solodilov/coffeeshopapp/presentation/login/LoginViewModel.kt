package com.solodilov.coffeeshopapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solodilov.coffeeshopapp.domain.usecase.LoginUseCase
import com.solodilov.coffeeshopapp.presentation.common.Result
import com.solodilov.coffeeshopapp.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Boolean?>>(UiState.Success(null))
    val uiState: StateFlow<UiState<Boolean?>> = _uiState

    private val _emailState = MutableStateFlow<String>("")

    private val _passwordState = MutableStateFlow<String>("")

    private val _emailError = MutableStateFlow<Boolean>(false)
    val emailError: StateFlow<Boolean> = _emailError

    private val _passwordError = MutableStateFlow<Boolean>(false)
    val passwordError: StateFlow<Boolean> = _passwordError

    fun onEmailTextChanged(login: String) {
        _emailState.value = login
        _emailError.value = login.isEmpty()
    }

    fun onPasswordTextChanged(password: String) {
        _passwordState.value = password
        _passwordError.value = password.isEmpty()
    }

    fun onLoginClicked() {
        val email = _emailState.value
        val password = _passwordState.value

        if (email.isNotEmpty() && password.isNotEmpty()) {
            login(email, password)
        } else {
            _emailError.value = email.isEmpty()
            _passwordError.value = password.isEmpty()
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            flow {
                emit(Result.Loading)
                try {
                    emit(Result.Success(loginUseCase(email, password)))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }.collect { result ->
                _uiState.value = when (result) {
                    is Result.Loading -> UiState.Loading
                    is Result.Success -> UiState.Success(result.data)
                    is Result.Error -> UiState.Error(result.exception)
                }
            }
        }
    }

    fun clearState() {
        _uiState.value = UiState.Success(null)
    }
}