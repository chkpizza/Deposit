package com.wantique.home.di

import com.wantique.base.di.FeatureScope
import com.wantique.home.ui.detail.DetailFragment
import com.wantique.home.ui.home.HomeFragment
import dagger.Subcomponent

@FeatureScope
@Subcomponent
interface HomeComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeComponent
    }

    fun inject(fragment: HomeFragment)
    fun inject(fragment: DetailFragment)
}