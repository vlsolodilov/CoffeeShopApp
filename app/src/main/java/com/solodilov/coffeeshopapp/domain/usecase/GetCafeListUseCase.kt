package com.solodilov.coffeeshopapp.domain.usecase

import com.solodilov.coffeeshopapp.domain.entity.Cafe
import com.solodilov.coffeeshopapp.domain.repository.CafeRepository
import javax.inject.Inject

class GetCafeListUseCase @Inject constructor(private val cafeRepository: CafeRepository) {

    suspend operator fun invoke(): List<Cafe> = cafeRepository.getCafeList()

}