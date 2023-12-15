package com.wantique.deposit

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.wantique.deposit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy { (supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment).navController }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        wideToEdge()
        setUpNavGraph()
    }

    private fun wideToEdge() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        } else {
            binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
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
}