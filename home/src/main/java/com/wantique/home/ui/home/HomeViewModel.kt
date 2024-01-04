package com.wantique.home.ui.home

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetShuffledDepositProductUseCase
import com.wantique.home.domain.usecase.GetHighestDepositByBankUseCase
import com.wantique.home.domain.usecase.GetHomeBannerUseCase
import com.wantique.home.domain.usecase.GetShuffledSavingProductUseCase
import com.wantique.home.ui.home.model.Banner
import com.wantique.home.ui.home.model.BannerHorizontal
import com.wantique.home.ui.home.model.DepositPreview
import com.wantique.home.ui.home.model.DepositGrid
import com.wantique.home.ui.home.model.DepositHorizontal
import com.wantique.home.ui.home.model.SavingPreview
import com.wantique.home.ui.home.model.SavingVertical
import com.wantique.home.ui.home.model.TopDeposit
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
    private val getShuffledDepositProductUseCase: GetShuffledDepositProductUseCase,
    private val getShuffledSavingProductUseCase: GetShuffledSavingProductUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _home = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val home = _home.asStateFlow()

    private val _navigateToTopProduct = MutableSharedFlow<SimpleModel>()
    val navigateToTopProduct = _navigateToTopProduct.asSharedFlow()
    
    private val _navigateToBanner = MutableSharedFlow<SimpleModel>()
    val navigateToBanner = _navigateToBanner.asSharedFlow()

    private val _navigateToDeposit = MutableSharedFlow<SimpleModel>()
    val navigateToDeposit = _navigateToDeposit.asSharedFlow()

    private val _navigateToMoreDeposit = MutableSharedFlow<Unit>()
    val navigateToMoreDeposit = _navigateToMoreDeposit.asSharedFlow()

    private val _navigateToSaving = MutableSharedFlow<SimpleModel>()
    val navigateToSaving = _navigateToSaving.asSharedFlow()

    private val _navigateToMoreSaving = MutableSharedFlow<Unit>()
    val navigateToMoreSaving = _navigateToMoreSaving.asSharedFlow()

    private lateinit var topDeposit: DepositHorizontal<SimpleModel>
    private lateinit var bannerHorizontal: BannerHorizontal<SimpleModel>
    private lateinit var deposit: DepositGrid<SimpleModel>
    private lateinit var savings: SavingVertical<SimpleModel>

    fun load() {
        if(isInitialized()) {
            return
        }
        viewModelScope.launch {
            getHighestDeposit()
            getHomeBanner()
            getDepositProduct()
            getSavingProduct()
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
                topDeposit = DepositHorizontal(it.getValue().title, SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().deposits.map { deposit ->
                        TopDeposit(deposit.uid, deposit.bankCode, deposit.title, deposit.description, deposit.maxRate, deposit.minRate, ::onTopDepositClickListener)
                    })
                })

                merge(topDeposit)
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
                bannerHorizontal = BannerHorizontal(SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().banners.map { banner ->
                        Banner(banner.url, ::onBannerClickListener)
                    })
                })

                merge(bannerHorizontal)
            }
        }.collect()
    }

    private suspend fun getDepositProduct() {
        safeFlow {
            getShuffledDepositProductUseCase()
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                deposit = DepositGrid(it.getValue().title, SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().deposits.map { _deposit ->
                        DepositPreview(_deposit.uid, _deposit.bankCode, _deposit.title, _deposit.description, _deposit.maxRate, _deposit.minRate, ::onDepositClickListener)
                    })
                }, ::onMoreDepositClickListener)
                merge(deposit)
            }
        }.collect()
    }

    private suspend fun getSavingProduct() {
        safeFlow {
            getShuffledSavingProductUseCase()
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                _errorState.value = e
            } ?: run {
                savings = SavingVertical(it.getValue().title, SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().savings.map { saving ->
                        SavingPreview(saving.uid, saving.bankCode, saving.title, saving.description, saving.maxRate, saving.minRate, ::onSavingClickListener)
                    })
                }, ::onMoreSavingClickListener)

                merge(savings)
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
    
    private fun onTopDepositClickListener(model: SimpleModel) {
        viewModelScope.launch {
            _navigateToTopProduct.emit(model)
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

    private fun onMoreDepositClickListener() {
        viewModelScope.launch {
            _navigateToMoreDeposit.emit(Unit)
        }
    }

    private fun onSavingClickListener(model: SimpleModel) {
        viewModelScope.launch {
            _navigateToSaving.emit(model)
        }
    }

    private fun onMoreSavingClickListener() {
        viewModelScope.launch {
            _navigateToMoreSaving.emit(Unit)
        }
    }
}