package com.wantique.home.ui.deposit

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetAllDepositProductUseCase
import com.wantique.home.ui.deposit.model.Deposit
import com.wantique.home.ui.deposit.model.DepositVertical
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DepositViewModel @Inject constructor(
    private val getAllDepositProductUseCase: GetAllDepositProductUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _deposit = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val deposit = _deposit.asStateFlow()

    private val _navigateToDetail = MutableSharedFlow<SimpleModel>()
    val navigateToDetail = _navigateToDetail.asSharedFlow()

    private val _navigateToBottomSheet = MutableSharedFlow<Int>()
    val navigateToBottomSheet = _navigateToBottomSheet.asSharedFlow()

    private lateinit var product: DepositVertical<Deposit>
    private var filter = 0

    private fun isInitialized() = deposit.value != null

    fun load() {
        if(isInitialized()) {
           return
        }

        viewModelScope.launch {
            getAllDepositProduct()
        }
    }

    fun filter(filter: Int) {
        this.filter = filter

        val filtered = when(filter) {
            0 -> {
                SimpleSubmittableState<SimpleModel>().apply {
                    submitList(product.simpleSubmittableState.getCurrentList().toMutableList().sortedBy { it.uid })
                }
            }
            1 -> {
                SimpleSubmittableState<SimpleModel>().apply {
                    submitList(product.simpleSubmittableState.getCurrentList().toMutableList().sortedByDescending { it.maxRate })
                }
            }
            2 -> {
                SimpleSubmittableState<SimpleModel>().apply {
                    submitList(product.simpleSubmittableState.getCurrentList().toMutableList().sortedBy { it.bankCode })
                }
            }
            else -> throw RuntimeException()
        }

        _deposit.value = SimpleSubmittableState<SimpleModel>().apply {
            submitList(listOf(DepositVertical(product.title, filter, filtered, product.onClickListener)))
        }
    }

    private suspend fun getAllDepositProduct() {
        safeFlow {
            getAllDepositProductUseCase()
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                _errorState.value = e
            } ?: run {
                product = DepositVertical(it.getValue().title, filter, SimpleSubmittableState<Deposit>().apply {
                    submitList(it.getValue().deposits.map {
                        Deposit(it.uid, it.bankCode, it.title, it.description, it.maxRate, it.minRate, ::onDepositClickListener)
                    })
                }, ::onFilterClickListener)

                merge(product)
            }
        }.collect()
    }

    private fun merge(model: SimpleModel) {
        _deposit.value?.let {
            it.submitList(it.getCurrentList().toMutableList().apply {
                add(model)
            })
        } ?: run {
            _deposit.value = SimpleSubmittableState<SimpleModel>().apply {
                submitList(listOf(model))
            }
        }
    }

    private fun onFilterClickListener() {
        viewModelScope.launch {
            _navigateToBottomSheet.emit(filter)
        }
    }

    private fun onDepositClickListener(model: SimpleModel) {
        viewModelScope.launch {
            _navigateToDetail.emit(model)
        }
    }
}