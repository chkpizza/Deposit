package com.wantique.home.ui.deposit

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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wantique.base.ui.BaseFragment
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.R
import com.wantique.home.databinding.FilterBottomSheetBinding
import com.wantique.home.databinding.FragmentDepositBinding
import com.wantique.home.di.HomeComponentProvider
import com.wantique.home.ui.deposit.model.Deposit
import com.wantique.home.ui.deposit.model.DepositFilter
import kotlinx.coroutines.launch
import javax.inject.Inject

class DepositFragment : BaseFragment<FragmentDepositBinding>(R.layout.fragment_deposit) {
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by lazy { ViewModelProvider(this, factory)[DepositViewModel::class.java] }
    private var bottomSheetDialog: BottomSheetDialog? = null

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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToDetail.collect {
                    if(it is Deposit) {
                        navigator.navigate(DepositFragmentDirections.actionDepositFragmentToDetailFragment(it.uid))
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToBottomSheet.collect {
                    showFilterBottomSheet(it)
                }
            }
        }
    }

    private fun showFilterBottomSheet(filter: Int) {
        /**
         * BottomSheetDialog 를 생성할 때는 Context 가 필요하므로 Fragment 에서 생성하는게 바람직함
         * 생성에 필요한 정보
         * 1. BottomSheetDialog Layout 정보
         * 2. BottomSheetDialog item list
         * 3. BottomSheetDialog item click listener
         * */
        /*
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view = FilterBottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog?.setContentView(view.root)

        view.model = SimpleSubmittableState<SimpleModel>().apply {
            submitList(resources.getStringArray(com.wantique.resource.R.array.product_filter_title).mapIndexed { index, title ->
                DepositFilter(title, index, index == filter, ::onFilterClickListener)
            })
        }

        bottomSheetDialog?.show()

         */
        bottomSheetDialog = BottomSheetDialog(requireActivity())
        FilterBottomSheetBinding.inflate(layoutInflater).apply {
            bottomSheetDialog?.setContentView(root)
            model = SimpleSubmittableState<SimpleModel>().apply {
                submitList(resources.getStringArray(com.wantique.resource.R.array.product_filter_title).mapIndexed { index, title ->
                    DepositFilter(title, index, index == filter, ::onFilterClickListener)
                })
            }
        }

        bottomSheetDialog?.show()
    }

    private fun onFilterClickListener(model: SimpleModel) {
        if(model is DepositFilter) {
            viewModel.filter(model.position)
        }
        bottomSheetDialog?.dismiss()
    }
}