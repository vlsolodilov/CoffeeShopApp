package com.solodilov.coffeeshopapp.domain.repository

import com.solodilov.coffeeshopapp.domain.entity.Cafe
import com.solodilov.coffeeshopapp.domain.entity.Coffee

interface CafeRepository {
    suspend fun getCafeList(): List<Cafe>
    suspend fun getCafeMenu(cafeId: Int): List<Coffee>
}