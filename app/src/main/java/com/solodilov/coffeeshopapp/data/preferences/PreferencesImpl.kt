package com.solodilov.coffeeshopapp.data.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : Preferences {

    private companion object {
        const val TOKEN = "token"
    }

    override fun getToken(): String? =
        sharedPreferences.getString(TOKEN, null)

    override fun setToken(token: String): Boolean =
        sharedPreferences.edit().run {
            putString(TOKEN, token)
            commit()
        }
}