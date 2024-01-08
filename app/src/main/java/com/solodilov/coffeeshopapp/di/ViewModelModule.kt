package com.solodilov.coffeeshopapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solodilov.coffeeshopapp.presentation.common.ViewModelFactory
import com.solodilov.coffeeshopapp.presentation.cafe_list.CafeListViewModel
import com.solodilov.coffeeshopapp.presentation.login.LoginViewModel
import com.solodilov.coffeeshopapp.presentation.menu.CafeMenuViewModel
import com.solodilov.coffeeshopapp.presentation.register.RegisterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CafeListViewModel::class)
    fun bindCafeListViewModel(viewModel: CafeListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CafeMenuViewModel::class)
    fun bindCafeMenuViewModel(viewModel: CafeMenuViewModel): ViewModel
}