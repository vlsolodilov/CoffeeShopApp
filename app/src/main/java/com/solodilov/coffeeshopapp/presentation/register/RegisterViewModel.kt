package com.solodilov.coffeeshopapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solodilov.coffeeshopapp.domain.usecase.RegisterUseCase
import com.solodilov.coffeeshopapp.presentation.common.Result
import com.solodilov.coffeeshopapp.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Boolean?>>(UiState.Success(null))
    val uiState: StateFlow<UiState<Boolean?>> = _uiState

    private val _emailState = MutableStateFlow<String>("")

    private val _passwordState = MutableStateFlow<String>("")

    private val _repeatedPasswordState = MutableStateFlow<String>("")

    private val _emailError = MutableStateFlow<Boolean>(false)
    val emailError: StateFlow<Boolean> = _emailError

    private val _passwordError = MutableStateFlow<Boolean>(false)
    val passwordError: StateFlow<Boolean> = _passwordError

    private val _repeatedPasswordError = MutableStateFlow<Boolean>(false)
    val repeatedPasswordError: StateFlow<Boolean> = _repeatedPasswordError

    fun onEmailTextChanged(login: String) {
        _emailState.value = login
        _emailError.value = login.isEmpty()
    }

    fun onPasswordTextChanged(password: String) {
        _passwordState.value = password
        _passwordError.value = password.isEmpty()
    }

    fun onRepeatedPasswordTextChanged(password: String) {
        _repeatedPasswordState.value = password
        _repeatedPasswordError.value = password != _passwordState.value
    }

    fun onRegisterClicked() {
        val email = _emailState.value
        val password = _passwordState.value
        val repeatedPassword = _repeatedPasswordState.value

        if (email.isNotEmpty() && password.isNotEmpty() && password == repeatedPassword) {
            register(email, password)
        } else {
            _emailError.value = email.isEmpty()
            _passwordError.value = password.isEmpty()
            _repeatedPasswordError.value = password != repeatedPassword
        }
    }

    private fun register(email: String, password: String) {
        viewModelScope.launch {
            flow {
                emit(Result.Loading)
                try {
                    emit(Result.Success(registerUseCase(email, password)))
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