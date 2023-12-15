package com.wantique.deposit

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import com.wantique.base.navigation.Navigator
import com.wantique.base.navigation.NavigatorProvider
import com.wantique.base.ui.BaseActivity
import com.wantique.deposit.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main), NavigatorProvider {
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment).navController }
    private val appNavigator by lazy { NavigatorImpl.getInstance(navController) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpNavGraph()
    }

    private fun setUpNavGraph() {
        val navGraph = navController.navInflater.inflate(R.navigation.app_nav_graph)
        if(getPreferences(MODE_PRIVATE).getBoolean("SIGN_IN", false)) {
            navGraph.setStartDestination(R.id.content_nav_graph)
        } else {
            navGraph.setStartDestination(R.id.init_nav_graph)
        }
        navController.graph = navGraph
    }

    override fun getNavigator() = appNavigator
}