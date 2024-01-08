package com.solodilov.coffeeshopapp.data.datasource.network

import com.solodilov.coffeeshopapp.data.preferences.Preferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferences: Preferences,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifyRequest = originalRequest.newBuilder().apply {
            preferences.getToken()?.let { token ->
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        return chain.proceed(modifyRequest)
    }
}