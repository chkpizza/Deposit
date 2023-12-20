package com.wantique.home.ui.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetAllDepositProductUseCase
import com.wantique.home.domain.usecase.GetHighestDepositByBankUseCase
import com.wantique.home.domain.usecase.GetHomeBannerUseCase
import com.wantique.home.ui.home.model.Banner
import com.wantique.home.ui.home.model.Banners
import com.wantique.home.ui.home.model.DepositNormal
import com.wantique.home.ui.home.model.DepositSmall
import com.wantique.home.ui.home.model.DepositsHorizontal
import com.wantique.home.ui.home.model.DepositsVertical
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getHighestDepositByBankUseCase: GetHighestDepositByBankUseCase,
    private val getHomeBannerUseCase: GetHomeBannerUseCase,
    private val getAllDepositProductUseCase: GetAllDepositProductUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _home = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val home = _home.asStateFlow()

    private val _navigateToBanner = MutableSharedFlow<SimpleModel>()
    val navigateToBanner = _navigateToBanner.asSharedFlow()

    private val _navigateToDeposit = MutableSharedFlow<SimpleModel>()
    val navigateToDeposit = _navigateToDeposit.asSharedFlow()

    private lateinit var summary: DepositsHorizontal<SimpleModel>
    private lateinit var banners: Banners<SimpleModel>
    private lateinit var depositsHorizontal: DepositsVertical<SimpleModel>

    fun load() {
        if(isInitialized()) {
            return
        }
        viewModelScope.launch {
            getHighestDeposit()
            getHomeBanner()
            getAllDepositProduct()
        }
    }

    private fun isInitialized(): Boolean {
        return home.value != null
    }

    private suspend fun getHighestDeposit() {
        safeFlow {
            getHighestDepositByBankUseCase()
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                _errorState.value = e
            } ?: run {
                summary = DepositsHorizontal(it.getValue().title, SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().deposits.map { deposit ->
                        DepositSmall(deposit.uid, deposit.bankCode, deposit.title, deposit.description, deposit.maxRate, deposit.minRate, deposit.rateDescription, deposit.taxFree, ::onDepositClickListener)
                    })
                })

                merge(summary)
            }
        }.collect()
    }

    private suspend fun getHomeBanner() {
        safeFlow {
            getHomeBannerUseCase()
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                _errorState.value = e
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

    private suspend fun getAllDepositProduct() {
        safeFlow {
            getAllDepositProductUseCase()
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                depositsHorizontal = DepositsVertical(it.getValue().title, SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().deposits.map { deposit ->
                        DepositNormal(deposit.uid, deposit.bankCode, deposit.title, deposit.description, deposit.maxRate, deposit.minRate, deposit.rateDescription, deposit.taxFree, :: onDepositClickListener)
                    })
                })

                merge(depositsHorizontal)
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

    private fun onBannerClickListener(model: SimpleModel) {
        viewModelScope.launch {
            _navigateToBanner.emit(model)
        }
    }

    private fun onDepositClickListener(model: SimpleModel) {
        viewModelScope.launch {
            _navigateToDeposit.emit(model)
        }
    }
}