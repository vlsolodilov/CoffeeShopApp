package com.solodilov.coffeeshopapp.presentation.cafe_list

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.solodilov.coffeeshopapp.domain.entity.LocationPoint
import com.solodilov.coffeeshopapp.domain.usecase.GetCafeListUseCase
import com.solodilov.coffeeshopapp.presentation.common.Result
import com.solodilov.coffeeshopapp.presentation.common.UiState
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CafeListViewModel @Inject constructor(
    private val getCafeListUseCase: GetCafeListUseCase,
    private val fusedLocationClient: FusedLocationProviderClient,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<CafeUi>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<CafeUi>>> = _uiState

    init {
        getData()
    }

    fun getData() {
        viewModelScope.launch {
            flow {
                emit(Result.Loading)
                try {
                    emit(Result.Success(getCafeUiList()))
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


    private suspend fun getCafeUiList(): List<CafeUi> {
        val cafeList = getCafeListUseCase()
        val location = try {
            fusedLocationClient.getCurrentLocation()
        } catch (e: Exception) {
            null
        }
        return if (location != null) {
            cafeList.map { cafe ->
                CafeUi(
                    id = cafe.id,
                    name = cafe.name,
                    point = cafe.point,
                    distance = calculateDistance(LocationPoint(location.latitude, location.longitude), cafe.point),
                )
            }
        } else {
            cafeList.map { cafe ->
                CafeUi(
                    id = cafe.id,
                    name = cafe.name,
                    point = cafe.point,
                    distance = null,
                )
            }
        }
    }

    private fun calculateDistance(location: LocationPoint, cafeLocation: LocationPoint): Int {
        val results = FloatArray(1)
        Location.distanceBetween(
            location.latitude, location.longitude,
            cafeLocation.latitude, cafeLocation.longitude,
            results
        )
        return results[0].toInt()
    }

    fun findMidpoint(cafes: List<CafeUi>): Point {
        var sumLat = 0.0
        var sumLng = 0.0

        cafes.forEach { cafe ->
            sumLat += cafe.point.latitude
            sumLng += cafe.point.longitude
        }

        val avgLat = sumLat / cafes.size
        val avgLng = sumLng / cafes.size

        return Point(avgLat, avgLng)
    }

    @SuppressLint("MissingPermission")
    private suspend fun FusedLocationProviderClient.getCurrentLocation(
        priority: Int = Priority.PRIORITY_BALANCED_POWER_ACCURACY
    ): Location = suspendCancellableCoroutine { cont ->
        val source = CancellationTokenSource()
        getCurrentLocation(priority, source.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(location)
                } else {
                    cont.resumeWithException(TimeoutException())
                }
            }
            .addOnFailureListener(cont::resumeWithException)

        cont.invokeOnCancellation { source.cancel() }
    }
}