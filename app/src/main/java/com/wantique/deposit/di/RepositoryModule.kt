package com.wantique.deposit.di

import com.wantique.auth.data.repository.AuthRepositoryImpl
import com.wantique.auth.domain.repository.AuthRepository
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
}