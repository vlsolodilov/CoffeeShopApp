package com.solodilov.coffeeshopapp.presentation.cafe_list

import com.solodilov.coffeeshopapp.domain.entity.LocationPoint

data class CafeUi(
    val id: Int,
    val name: String,
    val point: LocationPoint,
    val distance: Int?,
)
