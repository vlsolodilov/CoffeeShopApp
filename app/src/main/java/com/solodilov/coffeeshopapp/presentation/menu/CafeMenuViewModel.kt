package com.solodilov.coffeeshopapp.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solodilov.coffeeshopapp.domain.entity.Coffee
import com.solodilov.coffeeshopapp.domain.usecase.GetCafeMenuUseCase
import com.solodilov.coffeeshopapp.presentation.common.Result
import com.solodilov.coffeeshopapp.presentation.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class CafeMenuViewModel @Inject constructor(
    private val getCafeMenuUseCase: GetCafeMenuUseCase,
) : ViewModel() {

    private val _cafeId = MutableStateFlow<Int?>(null)

    private val _uiState = MutableStateFlow<UiState<List<Coffee>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Coffee>>> = _uiState


    init {
        viewModelScope.launch {
            _cafeId
                .filterNotNull()
                .distinctUntilChanged()
                .onEach { cafeId ->
                    getData(cafeId)
                }
                .collect()
        }
    }

    fun setCafeId(cafeId: Int) {
        _cafeId.value = cafeId
    }

    private fun getData(cafeId: Int) {
        viewModelScope.launch {
            flow {
                emit(Result.Loading)
                try {
                    emit(Result.Success(getCafeMenuUseCase(cafeId)))
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

    fun refreshData() {
        _cafeId.value?.let { cafeId ->
            getData(cafeId)
        }
    }

    fun increaseQuantity(coffeeId: Int) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            val updatedCoffees = currentState.data.map { coffee ->
                if (coffee.id == coffeeId) {
                    coffee.copy(quantity = coffee.quantity + 1)
                } else {
                    coffee
                }
            }
            _uiState.value = UiState.Success(updatedCoffees)
        }
    }

    fun decreaseQuantity(coffeeId: Int) {
        val currentState = _uiState.value
        if (currentState is UiState.Success) {
            val updatedCoffees = currentState.data.map { coffee ->
                if (coffee.id == coffeeId && coffee.quantity > 0) {
                    coffee.copy(quantity = coffee.quantity - 1)
                } else {
                    coffee
                }
            }
            _uiState.value = UiState.Success(updatedCoffees)
        }
    }
}