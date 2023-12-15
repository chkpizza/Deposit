package com.wantique.deposit

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.wantique.base.navigation.Navigator

class NavigatorImpl private constructor(
    private val navController: NavController
) : Navigator {
    override fun navigateToInit() {
        val navGraph = navController.navInflater.inflate(R.navigation.app_nav_graph)
        navGraph.setStartDestination(R.id.init_nav_graph)
        navController.graph = navGraph
    }

    override fun navigateToContent() {
        val navGraph = navController.navInflater.inflate(R.navigation.app_nav_graph)
        navGraph.setStartDestination(R.id.content_nav_graph)
        navController.graph = navGraph
    }

    override fun navigate(id: Int) {
        navController.navigate(id)
    }

    override fun navigate(direction: NavDirections) {
        navController.navigate(direction)
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    companion object {
        private lateinit var navigator: Navigator

        fun getInstance(navController: NavController): Navigator {
            if(!::navigator.isInitialized) {
                navigator = NavigatorImpl(navController)
            }

            return navigator
        }
    }
}