package com.wantique.home.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navGraphViewModels
import com.wantique.base.ui.BaseFragment
import com.wantique.home.R
import com.wantique.home.databinding.FragmentHomeBinding
import com.wantique.home.di.HomeComponentProvider
import com.wantique.home.ui.home.model.DepositPreview
import com.wantique.home.ui.home.model.TopDeposit
import com.wantique.resource.Constant
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    @Inject lateinit var factory: ViewModelProvider.Factory
    private val viewModel by navGraphViewModels<HomeViewModel>(R.id.home_nav_graph) { factory }

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    private var clickTime: Long = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as HomeComponentProvider).getHomeComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(System.currentTimeMillis() - clickTime >= Constant.BACK_PRESS_INTERVAL) {
                    clickTime = System.currentTimeMillis()
                    Toast.makeText(requireActivity(), getString(com.wantique.resource.R.string.home_back_press_notice), Toast.LENGTH_SHORT).show()
                } else {
                    requireActivity().finish()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        return super.onCreateView(inflater, container, savedInstanceState)
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

    /**
     * [issue]
     * navigateToDeposit 을 collect 하기 위해 기존에는 lifecycleScope 범위의 Coroutine 을 생성했었는데
     * 이럴 경우 DetailFragment 로 navigate 하더라도 HomeFragment instance 는 삭제되지 않으며, navigateUp 으로 다시 HomeFragment 로 돌아왔을 때
     * lifecycleScope 의 Coroutine 을 다시 생성하게 되며 최종적으로 navigateToDeposit StateFlow 를 총 2곳의 Coroutine 에서 collect 하게되어 Navigation 시 Exception 이 발생하게 된다.
     * 이 문제를 해결하기 위해 Fragment 에서 Flow 를 collect 할 때는 viewLifecycleOwner 범위의 lifecycleScope 를 사용해야 한다.
     * 다른 화면으로 navigate 할 경우 Fragment 의 View 는 파괴되며 viewLifecycleOwner 범위에서 실행된 Coroutine 도 종료되게 되어서 2개의 Coroutine 이 Flow 를 collect 하는 문제를 방지할 수 있다.
     * */
    private fun setUpNavigateListener() {
        viewLifecycleOwner.lifecycleScope.launch { 
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToTopProduct.collect {
                    if(it is TopDeposit) {
                        navigator.navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.uid))
                    }
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToDeposit.collect {
                    if(it is DepositPreview) {
                        navigator.navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.uid))
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToBanner.collect {
                    Toast.makeText(requireActivity(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToMoreDeposit.collect {
                    navigator.navigate(HomeFragmentDirections.actionHomeFragmentToDepositFragment())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }
}