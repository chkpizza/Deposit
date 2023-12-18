package com.wantique.home.domain.model

import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
import com.wantique.home.ui.model.Deposits

data class Deposits (
    val title: String,
    val deposits: List<Deposit>,
) {
    fun asUi(): Deposits<SimpleModel> {
        return Deposits(title, SimpleSubmittableState<SimpleModel>().apply {
            submitList(deposits.map { it.asUi() })
        })
    }
}