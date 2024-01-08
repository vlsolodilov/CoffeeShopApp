package com.solodilov.coffeeshopapp.data.mapper

import com.solodilov.coffeeshopapp.data.model.CafeDto
import com.solodilov.coffeeshopapp.data.model.CoffeeDto
import com.solodilov.coffeeshopapp.domain.entity.Cafe
import com.solodilov.coffeeshopapp.domain.entity.Coffee
import com.solodilov.coffeeshopapp.domain.entity.LocationPoint
import javax.inject.Inject

class CafeMapper @Inject constructor() {
    fun mapCafeDtoToCafe(cafeDto: CafeDto): Cafe =
        Cafe(
            id = cafeDto.id,
            name = cafeDto.name,
            point = LocationPoint(
                cafeDto.pointDto.latitude.toDoubleOrNull() ?: 0.0,
                cafeDto.pointDto.longitude.toDoubleOrNull() ?: 0.0,
            )
        )
    fun mapCoffeeDtoToCoffee(coffeeDto: CoffeeDto): Coffee =
        Coffee(
            id = coffeeDto.id,
            name = coffeeDto.name,
            imageURL= coffeeDto.imageURL,
            price = coffeeDto.price,
            quantity = 0,
        )
}