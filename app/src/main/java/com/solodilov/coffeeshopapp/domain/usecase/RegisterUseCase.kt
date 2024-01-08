package com.solodilov.coffeeshopapp.domain.usecase

import com.solodilov.coffeeshopapp.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend operator fun invoke(name: String, password: String): Boolean =
        userRepository.register(name, password)
}