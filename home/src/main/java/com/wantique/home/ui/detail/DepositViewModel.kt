package com.wantique.home.ui.detail

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import com.wantique.home.domain.usecase.GetDepositHeaderUseCase
import com.wantique.home.ui.detail.model.DepositHeader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DepositViewModel @Inject constructor(
    private val getDepositHeaderUseCase: GetDepositHeaderUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _detail = MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val detail = _detail.asStateFlow()

    private lateinit var header: DepositHeader

    fun load(uid: String) {
        if(isInitialized()) {
            return
        }

        viewModelScope.launch {
            getDepositHeader(uid)
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
}