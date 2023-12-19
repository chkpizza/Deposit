package com.wantique.home.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.wantique.base.ui.BaseFragment
import com.wantique.home.R
import com.wantique.home.data.repository.HomeRepositoryImpl
import com.wantique.home.databinding.FragmentHomeBinding
import com.wantique.home.di.HomeComponentProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by navGraphViewModels<HomeViewModel>(R.id.home_nav_graph) { factory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as HomeComponentProvider).getHomeComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupInsets()
        setUpNavigateListener()

        viewModel.load()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            view.updatePadding(
                top = insets.systemWindowInsets.top,
                bottom = insets.systemWindowInsets.bottom
            )
            insets
        }
    }

    private fun setUpNavigateListener() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToDeposit.collect {
                    Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToBanner.collect {
                    Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun test() {
        lifecycleScope.launch {
            HomeRepositoryImpl(Dispatchers.IO).registerDeposit()
        }
    }
}