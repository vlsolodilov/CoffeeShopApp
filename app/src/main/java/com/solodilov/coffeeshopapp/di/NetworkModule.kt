package com.solodilov.coffeeshopapp.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.solodilov.coffeeshopapp.data.datasource.network.AuthInterceptor
import com.solodilov.coffeeshopapp.data.datasource.network.SevenWindsApi
import com.solodilov.coffeeshopapp.data.preferences.Preferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

	private companion object {

		const val BASE_URL = "http://147.78.66.203:3210/"
	}

	@Provides
	fun provideGsonConverterFactory(): GsonConverterFactory =
		GsonConverterFactory.create()

	@Singleton
	@Provides
	fun providesOkHttpClient(preferences: Preferences): OkHttpClient = OkHttpClient.Builder()
		.addNetworkInterceptor(AuthInterceptor(preferences))
		.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
		.build()


	@Provides
	@Singleton
	fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
		okHttpClient: OkHttpClient,
	): Retrofit =
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(gsonConverterFactory)
			.client(okHttpClient)
			.build()

	@Provides
	@Singleton
	fun provideSevenWindsApi(
		retrofit: Retrofit,
	): SevenWindsApi =
		retrofit.create(SevenWindsApi::class.java)

	@Provides
	@Singleton
	fun provideFusedLocationProviderClient(application: Application): FusedLocationProviderClient {
		return LocationServices.getFusedLocationProviderClient(application)
	}
}