package com.wantique.home.ui.detail

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetDepositBodyUseCase
import com.wantique.home.domain.usecase.GetDepositHeaderUseCase
import com.wantique.home.domain.usecase.GetSavingBodyUseCase
import com.wantique.home.domain.usecase.GetSavingHeaderUseCase
import com.wantique.home.ui.detail.model.DepositBody
import com.wantique.home.ui.detail.model.DepositHeader
import com.wantique.home.ui.detail.model.SavingBody
import com.wantique.home.ui.detail.model.SavingHeader
import com.wantique.resource.Constant
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val getDepositHeaderUseCase: GetDepositHeaderUseCase,
    private val getDepositBodyUseCase: GetDepositBodyUseCase,
    private val getSavingHeaderUseCase: GetSavingHeaderUseCase,
    private val getSavingBodyUseCase: GetSavingBodyUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _detail = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val detail = _detail.asStateFlow()

    private lateinit var header: SimpleModel
    private lateinit var body: SimpleModel

    private val _navigateToDetail = MutableSharedFlow<SimpleModel>()
    val navigateToDetail = _navigateToDetail.asSharedFlow()

    fun load(uid: String, type: String) {
        if(isInitialized()) {
            return
        }

        viewModelScope.launch {
            when(type) {
                Constant.DEPOSIT -> {
                    getDepositHeader(uid)
                    getDepositBody(uid)
                }
                Constant.SAVING -> {
                    getSavingHeader(uid)
                    getSavingBody(uid)
                }
            }
        }
    }

    private fun isInitialized(): Boolean {
        return detail.value != null
    }

    private suspend fun getDepositHeader(uid: String) {
        safeFlow {
            getDepositHeaderUseCase(uid)
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                it.getValue().apply {
                    header = DepositHeader(uid, bankCode, title, description, maxRate, minRate, rateDescription, taxFree)
                }
                merge(header)
            }
        }.collect()
    }

    private suspend fun getSavingHeader(uid: String) {
        safeFlow {
            getSavingHeaderUseCase(uid)
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                _errorState.value = e
            } ?: run {
                it.getValue().apply {
                    header = SavingHeader(uid, bankCode, title, description, maxRate, minRate, rateDescription, taxFree)
                }
            }
            merge(header)
        }.collect()
    }

    private suspend fun getDepositBody(uid: String) {
        safeFlow {
            getDepositBodyUseCase(uid)
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                it.getValue().apply {
                    body = DepositBody(uid, bankCode, signUpMethod, target, contractPeriod, signUpAmount, protect, url, ::onBodyClickListener)
                }
                merge(body)
            }
        }.collect()
    }

    private suspend fun getSavingBody(uid: String) {
        safeFlow {
            getSavingBodyUseCase(uid)
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                _errorState.value = e
            } ?: run {
                it.getValue().apply {
                    body = SavingBody(uid, bankCode, signUpMethod, target, contractPeriod, signUpAmount, protect, url, ::onBodyClickListener)
                }
            }
            merge(body)
        }.collect()
    }


    private fun merge(model: SimpleModel) {
        _detail.value?.let {
            it.submitList(it.getCurrentList().toMutableList().apply {
                add(model)
            })
        } ?: run {
            _detail.value = SimpleSubmittableState<SimpleModel>().apply {
                submitList(listOf(model))
            }
        }
    }

    private fun onBodyClickListener(model: SimpleModel) {
        viewModelScope.launch {
            _navigateToDetail.emit(model)
        }
    }
}