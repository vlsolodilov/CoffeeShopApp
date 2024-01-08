package com.solodilov.coffeeshopapp.di

import com.solodilov.coffeeshopapp.data.preferences.Preferences
import com.solodilov.coffeeshopapp.data.preferences.PreferencesImpl
import com.solodilov.coffeeshopapp.data.repository.CafeRepositoryImpl
import com.solodilov.coffeeshopapp.data.repository.UserRepositoryImpl
import com.solodilov.coffeeshopapp.domain.repository.CafeRepository
import com.solodilov.coffeeshopapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataModule {

	@Singleton
	@Binds
	fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

	@Singleton
	@Binds
	fun bindCafeRepository(impl: CafeRepositoryImpl): CafeRepository

	@Singleton
	@Binds
	fun bindPreferences(impl: PreferencesImpl): Preferences


}