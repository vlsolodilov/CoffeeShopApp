package com.solodilov.coffeeshopapp.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("tokenLifetime")
    val tokenLifetime: Int,
)