package com.wantique.home.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetHighestDepositByBankUseCase
import com.wantique.home.ui.model.Deposit
import com.wantique.home.ui.model.Deposits
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getHighestDepositByBankUseCase: GetHighestDepositByBankUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _home = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val home = _home.asStateFlow()

    private lateinit var deposits: Deposits<SimpleModel>

    fun getDepositByBank() {
        if(isInitialized()) {
            return
        }
        viewModelScope.launch {
            safeFlow {
                getHighestDepositByBankUseCase()
            }.onEach {
                it.isErrorOrNull()?.let {

                } ?: run {
                    deposits = it.getValue()
                    merge()
                }
            }.collect()
        }
    }

    private fun merge() {
        _home.value = SimpleSubmittableState<SimpleModel>().apply {
            submitList(mutableListOf<SimpleModel>().apply {
                add(deposits)
            })
        }
    }

    private fun isInitialized() = home.value != null
}