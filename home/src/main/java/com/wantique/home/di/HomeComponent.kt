package com.wantique.home.di

import com.wantique.base.di.FeatureScope
import com.wantique.home.ui.HomeFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(fragment: HomeFragment)
}