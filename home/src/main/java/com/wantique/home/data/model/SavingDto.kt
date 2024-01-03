package com.wantique.home.data.model

import com.wantique.home.domain.model.Saving

data class SavingDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val maxRate: Double? = null,
    val minRate: Double? = null
) {
    fun asDomain(): Saving {
        return Saving(uid!!, bankCode!!, title!!, description!!, maxRate!!, minRate!!)
    }
}
