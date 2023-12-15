package com.wantique.base.navigation

import androidx.navigation.NavDirections

interface Navigator {
    fun navigateToInit()
    fun navigateToContent()
    fun navigate(id: Int)
    fun navigate(direction: NavDirections)
    fun navigateUp()
}