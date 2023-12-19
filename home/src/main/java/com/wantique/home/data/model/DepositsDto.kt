package com.wantique.home.data.model

import com.wantique.home.domain.model.Deposit
import com.wantique.home.domain.model.Deposits

data class DepositsDto(
    val title: String = "",
    val deposits: List<DepositDto> = emptyList(),
) {
    fun asDomain(): Deposits {
        return Deposits(title, deposits.map { it.asDomain() })
    }
}