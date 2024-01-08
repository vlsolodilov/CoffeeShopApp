package com.solodilov.coffeeshopapp.domain.usecase

import com.solodilov.coffeeshopapp.domain.entity.Coffee
import com.solodilov.coffeeshopapp.domain.repository.CafeRepository
import javax.inject.Inject

class GetCafeMenuUseCase @Inject constructor(private val cafeRepository: CafeRepository) {

    suspend operator fun invoke(cafeId: Int): List<Coffee> = cafeRepository.getCafeMenu(cafeId)

}