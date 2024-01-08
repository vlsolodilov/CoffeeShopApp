package com.solodilov.coffeeshopapp.data.repository

import com.solodilov.coffeeshopapp.data.datasource.network.SevenWindsApi
import com.solodilov.coffeeshopapp.data.model.Auth
import com.solodilov.coffeeshopapp.data.preferences.Preferences
import com.solodilov.coffeeshopapp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: SevenWindsApi,
    private val preferences: Preferences,
): UserRepository {

    override suspend fun login(name: String, password: String): Boolean {
        return try {
            val response = api.login(Auth(name, password))
            preferences.setToken(response.token)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun register(name: String, password: String): Boolean {
        return try {
            val response = api.register(Auth(name, password))
            preferences.setToken(response.token)
        } catch (e: Exception) {
            false
        }
    }
}