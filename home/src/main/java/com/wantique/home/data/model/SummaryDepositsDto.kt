package com.wantique.home.data.model

import androidx.annotation.Keep
import com.wantique.home.domain.model.SummaryDeposits

@Keep
data class SummaryDepositsDto(
    val title: String = "",
    val deposits: List<SummaryDepositDto> = emptyList()
) {
    fun asDomain(): SummaryDeposits {
        return SummaryDeposits(title, deposits.map { it.asDomain() })
    }
}