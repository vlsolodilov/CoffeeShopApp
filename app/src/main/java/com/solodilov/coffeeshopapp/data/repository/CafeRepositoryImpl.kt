package com.solodilov.coffeeshopapp.data.repository

import com.solodilov.coffeeshopapp.data.datasource.network.SevenWindsApi
import com.solodilov.coffeeshopapp.data.mapper.CafeMapper
import com.solodilov.coffeeshopapp.domain.entity.Cafe
import com.solodilov.coffeeshopapp.domain.entity.Coffee
import com.solodilov.coffeeshopapp.domain.repository.CafeRepository
import javax.inject.Inject

class CafeRepositoryImpl @Inject constructor(
    private val api: SevenWindsApi,
    private val mapper: CafeMapper,
): CafeRepository {

    override suspend fun getCafeList(): List<Cafe> =
        api.getCafeList().map { cafeDto ->  
            mapper.mapCafeDtoToCafe(cafeDto)
        }

    override suspend fun getCafeMenu(cafeId: Int): List<Coffee> =
        api.getCafeMenu(cafeId).map { coffeeDto ->
            mapper.mapCoffeeDtoToCoffee(coffeeDto)
        }
}