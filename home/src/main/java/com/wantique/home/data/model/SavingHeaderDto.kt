package com.wantique.home.data.model

import com.wantique.home.domain.model.SavingHeader

data class SavingHeaderDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val maxRate: Double? = null,
    val minRate: Double? = null,
    val rateDescription: String? = null,
    val taxFree: Boolean? = null,
) {
    fun asDomain(): SavingHeader {
        return SavingHeader(uid!!, bankCode!!, title!!, description!!, maxRate!!, minRate!!, rateDescription!!, taxFree!!)
    }
}