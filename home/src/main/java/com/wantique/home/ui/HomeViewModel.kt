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
import com.wantique.home.ui.model.Banner
import com.wantique.home.ui.model.Banners
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
    private lateinit var banners: Banners<SimpleModel>

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

                    //임시 코드
                    banners = Banners(SimpleSubmittableState<SimpleModel>().apply {
                        submitList(listOf(Banner("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg"), Banner("https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2022/05/22/f1c66ccb-f5bf-4382-af54-96ba8f2d3fb5.jpg")))
                    })
                    merge()
                }
            }.collect()
        }
    }

    private fun merge() {


        _home.value = SimpleSubmittableState<SimpleModel>().apply {
            submitList(mutableListOf<SimpleModel>().apply {
                add(deposits)
                add(banners)
            })
        }
    }

    private fun isInitialized() = home.value != null
}