package com.solodilov.coffeeshopapp.domain.repository

interface UserRepository {

    suspend fun login(name: String, password: String): Boolean
    suspend fun register(name: String, password: String): Boolean
}