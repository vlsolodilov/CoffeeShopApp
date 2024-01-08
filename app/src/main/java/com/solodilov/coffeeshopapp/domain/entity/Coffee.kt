package com.solodilov.coffeeshopapp.domain.entity

data class Coffee(
    val id: Int,
    val name: String,
    val imageURL: String,
    val price: Int,
    val quantity: Int,
)
