package com.solodilov.coffeeshopapp.data.model

import com.google.gson.annotations.SerializedName

data class PointDto(
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
)