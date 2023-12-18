package com.wantique.home.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.domain.model.Deposit
import com.wantique.home.domain.model.HorizontalDeposit
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _home = MutableLiveData<SimpleSubmittableState<SimpleModel>>()
    val home: LiveData<SimpleSubmittableState<SimpleModel>> get() = _home

    private lateinit var horizontalDeposit: HorizontalDeposit<SimpleModel>

    fun test() {
        makeHorizontalDeposit()

        _home.value = SimpleSubmittableState<SimpleModel>().apply {
            submitList(mutableListOf<SimpleModel>().apply {
                add(horizontalDeposit)
            })
        }
    }

    private fun makeHorizontalDeposit() {
        horizontalDeposit = HorizontalDeposit(SimpleSubmittableState<SimpleModel>().apply {
            submitList(listOf(
                Deposit("https://news.koreadaily.com/data/photo/2023/09/21/202309211503778535_650be03e9459a.jpeg", "국민수퍼정기예금(개인)", "1" ,""),
                Deposit("https://news.koreadaily.com/data/photo/2023/09/21/202309211503778535_650be03e9459a.jpeg", "신한 My플러스 정기예금", "2" ,""),
                Deposit("https://news.koreadaily.com/data/photo/2023/09/21/202309211503778535_650be03e9459a.jpeg", "NH고향사랑기부예금", "3" ,""),
            ))
        }, "은행별 최고 금리 예금 상품")
    }
}