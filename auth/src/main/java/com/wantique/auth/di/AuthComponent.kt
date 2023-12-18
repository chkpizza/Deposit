package com.wantique.auth.di

import com.wantique.auth.ui.AuthFragment
import com.wantique.base.di.FeatureScope
import dagger.Subcomponent

@FeatureScope
@Subcomponent
interface AuthComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AuthComponent
    }

    fun inject(fragment: AuthFragment)
}