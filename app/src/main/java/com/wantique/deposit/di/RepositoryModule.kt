package com.wantique.deposit.di

import com.wantique.auth.data.repository.AuthRepositoryImpl
import com.wantique.auth.domain.repository.AuthRepository
import com.wantique.home.data.repository.HomeRepositoryImpl
import com.wantique.home.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideAuthRepository(dispatcher: CoroutineDispatcher): AuthRepository {
        return AuthRepositoryImpl(dispatcher)
    }

    @Singleton
    @Provides
    fun provideHomeRepository(dispatcher: CoroutineDispatcher): HomeRepository {
        return HomeRepositoryImpl(dispatcher)
    }
}