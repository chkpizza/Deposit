package com.wantique.deposit.di

import android.content.Context
import com.wantique.base.network.NetworkStateTracker
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
class NetworkStateTrackerModule {
    @Provides
    fun provideNetworkStateTracker(context: Context): NetworkStateTracker {
        return NetworkStateTracker(context)
    }
}