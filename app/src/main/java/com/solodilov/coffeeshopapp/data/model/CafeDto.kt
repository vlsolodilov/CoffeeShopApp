package com.solodilov.coffeeshopapp.data.model

import com.google.gson.annotations.SerializedName

data class CafeDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("point")
    val pointDto: PointDto,
)