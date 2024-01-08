package com.solodilov.coffeeshopapp.data.preferences

interface Preferences {

    fun getToken(): String?
    fun setToken(token: String): Boolean
}