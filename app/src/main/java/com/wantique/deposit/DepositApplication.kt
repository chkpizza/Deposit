package com.wantique.deposit

import android.app.Application
import com.wantique.auth.di.AuthComponent
import com.wantique.auth.di.AuthComponentProvider
import com.wantique.deposit.di.DaggerAppComponent
import com.wantique.home.di.HomeComponent
import com.wantique.home.di.HomeComponentProvider

class DepositApplication : Application(), AuthComponentProvider, HomeComponentProvider {
    private val appComponent by lazy { DaggerAppComponent.factory().create() }

    override fun getAuthComponent(): AuthComponent {
        return appComponent.getAuthComponent().create()
    }

    override fun getHomeComponent(): HomeComponent {
        return appComponent.getHomeComponent().create()
    }

}