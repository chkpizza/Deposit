package com.wantique.home.data.model

import com.wantique.home.domain.model.SummaryDeposits

data class SummaryDepositsDto(
    val title: String = "",
    val deposits: List<SummaryDepositDto> = emptyList()
) {
    fun asDomain(): SummaryDeposits {
        return SummaryDeposits(title, deposits.map { it.asDomain() })
    }
}