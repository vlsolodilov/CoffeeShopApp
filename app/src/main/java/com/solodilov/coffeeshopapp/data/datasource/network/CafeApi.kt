package com.solodilov.coffeeshopapp.data.datasource.network

import com.solodilov.coffeeshopapp.data.model.Auth
import com.solodilov.coffeeshopapp.data.model.AuthResponse
import com.solodilov.coffeeshopapp.data.model.CafeDto
import com.solodilov.coffeeshopapp.data.model.CoffeeDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SevenWindsApi {

    @POST("auth/register")
    suspend fun register(@Body auth: Auth): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body auth: Auth): AuthResponse

    @GET("locations")
    suspend fun getCafeList(): List<CafeDto>

    @GET("location/{id}/menu")
    suspend fun getCafeMenu(@Path("id") cafeId: Int): List<CoffeeDto>
}