package com.wantique.home.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetHighestDepositByBankUseCase
import com.wantique.home.domain.usecase.GetHomeBannerUseCase
import com.wantique.home.ui.model.Banner
import com.wantique.home.ui.model.Banners
import com.wantique.home.ui.model.Deposit
import com.wantique.home.ui.model.Deposits
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getHighestDepositByBankUseCase: GetHighestDepositByBankUseCase,
    private val getHomeBannerUseCase: GetHomeBannerUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _home = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val home = _home.asStateFlow()

    private val _navigateToBanner = MutableSharedFlow<Banner>()
    val navigateToBanner = _navigateToBanner.asSharedFlow()

    private val _navigateToDeposit = MutableSharedFlow<Deposit>()
    val navigateToDeposit = _navigateToDeposit.asSharedFlow()

    private lateinit var deposits: Deposits<SimpleModel>
    private lateinit var banners: Banners<SimpleModel>

    fun load() {
        if(isInitialized()) {
            return
        }
        viewModelScope.launch {
            getHighestDeposit()
            getHomeBanner()
        }
    }

    private fun isInitialized(): Boolean {
        return home.value != null
    }

    private suspend fun getHighestDeposit() {
        safeFlow {
            getHighestDepositByBankUseCase()
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                deposits = Deposits(it.getValue().title, SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().deposits.map { deposit ->
                        Deposit(deposit.icon, deposit.title, deposit.maximum, deposit.minimum, ::onDepositClickListener)
                    })
                })

                merge(deposits)
            }
        }.collect()
    }

    private suspend fun getHomeBanner() {
        safeFlow {
            getHomeBannerUseCase()
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                banners = Banners(SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().banners.map { banner ->
                        Banner(banner.url, ::onBannerClickListener)
                    })
                })

                merge(banners)
            }
        }.collect()
    }

    private fun merge(model: SimpleModel) {
        _home.value?.let {
            it.submitList(it.getCurrentList().toMutableList().apply {
                add(model)
            })
        } ?: run {
            _home.value = SimpleSubmittableState<SimpleModel>().apply {
                submitList(listOf(model))
            }
        }
    }

    private fun onBannerClickListener(model: Banner) {
        viewModelScope.launch {
            _navigateToBanner.emit(model)
        }
    }

    private fun onDepositClickListener(model: Deposit) {
        viewModelScope.launch {
            _navigateToDeposit.emit(model)
        }
    }
}