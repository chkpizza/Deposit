package com.wantique.deposit.di

import com.wantique.auth.di.AuthComponent
import com.wantique.home.di.HomeComponent
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [SubComponentModule::class, ViewModelFactoryModule::class, ViewModelModule::class, DispatcherModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun getAuthComponent(): AuthComponent.Factory
    fun getHomeComponent(): HomeComponent.Factory
}

@Module(subcomponents = [AuthComponent::class, HomeComponent::class])
object SubComponentModule