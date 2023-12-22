package com.wantique.home.data.model

import androidx.annotation.Keep
import com.wantique.home.domain.model.SummaryDeposit

@Keep
data class SummaryDepositDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val maxRate: Double? = null,
    val minRate: Double? = null
) {
    fun asDomain(): SummaryDeposit {
        return SummaryDeposit(uid!!, bankCode!!, title!!, description!!, maxRate!!, minRate!!)
    }
}
