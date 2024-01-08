package com.solodilov.coffeeshopapp

import android.app.Application
import com.solodilov.coffeeshopapp.di.DaggerApplicationComponent
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    val appComponent = DaggerApplicationComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
    }
}