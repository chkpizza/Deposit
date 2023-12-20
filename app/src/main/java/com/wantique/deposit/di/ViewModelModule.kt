package com.wantique.deposit.di

import androidx.lifecycle.ViewModel
import com.wantique.auth.ui.AuthViewModel
import com.wantique.base.di.ViewModelKey
import com.wantique.home.ui.detail.DepositViewModel
import com.wantique.home.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DepositViewModel::class)
    abstract fun bindDepositViewModel(viewModel: DepositViewModel): ViewModel
}