package com.wantique.home.ui.detail

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.wantique.base.ui.BaseFragment
import com.wantique.home.R
import com.wantique.home.databinding.FragmentDepositBinding

class DepositFragment : BaseFragment<FragmentDepositBinding>(R.layout.fragment_deposit) {
    private val args: DepositFragmentArgs by navArgs()
    private val uid by lazy { args.uid }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}