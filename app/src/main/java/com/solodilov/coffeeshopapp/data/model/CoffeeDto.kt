package com.solodilov.coffeeshopapp.data.model

import com.google.gson.annotations.SerializedName

data class CoffeeDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("imageURL")
    val imageURL: String,
    @SerializedName("price")
    val price: Int,
)