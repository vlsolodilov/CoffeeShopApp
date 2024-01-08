package com.solodilov.coffeeshopapp.di

import android.app.Application
import com.solodilov.coffeeshopapp.presentation.cafe_list.CafeListFragment
import com.solodilov.coffeeshopapp.presentation.cafe_list_map.CafeListMapFragment
import com.solodilov.coffeeshopapp.presentation.login.LoginFragment
import com.solodilov.coffeeshopapp.presentation.menu.CafeMenuFragment
import com.solodilov.coffeeshopapp.presentation.order_details.OrderDetailsFragment
import com.solodilov.coffeeshopapp.presentation.register.RegisterFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
	DataModule::class,
	PreferencesModule::class,
	NetworkModule::class,
	ViewModelModule::class,
])
interface ApplicationComponent {

	fun inject(fragment: LoginFragment)
	fun inject(fragment: RegisterFragment)
	fun inject(fragment: CafeListFragment)
	fun inject(fragment: CafeMenuFragment)
	fun inject(fragment: OrderDetailsFragment)
	fun inject(fragment: CafeListMapFragment)

	@Component.Factory
	interface Factory {
		fun create(
			@BindsInstance application: Application,
		): ApplicationComponent
	}
}