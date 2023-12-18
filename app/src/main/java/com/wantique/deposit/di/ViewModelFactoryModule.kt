package com.wantique.deposit.di

import androidx.lifecycle.ViewModelProvider
import com.wantique.base.ui.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}