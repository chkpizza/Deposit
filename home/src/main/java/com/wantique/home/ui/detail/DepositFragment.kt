package com.wantique.home.ui.detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.wantique.base.ui.BaseFragment
import com.wantique.home.R
import com.wantique.home.databinding.FragmentDepositBinding
import com.wantique.home.di.HomeComponentProvider
import javax.inject.Inject

class DepositFragment : BaseFragment<FragmentDepositBinding>(R.layout.fragment_deposit) {
    private val args: DepositFragmentArgs by navArgs()
    private val uid by lazy { args.uid }

    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProvider(this, factory)[DepositViewModel::class.java]}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as HomeComponentProvider).getHomeComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.load(uid)
    }
}