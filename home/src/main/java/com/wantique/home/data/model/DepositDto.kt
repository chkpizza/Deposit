package com.wantique.home.data.model

import com.wantique.home.domain.model.Deposit

data class DepositDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val icon: String? = null,
    val title: String? = null,
    val description: String? = null,
    val maximum: Double? = null,
    val minimum: Double? = null,
) {
    fun asDomain(): Deposit {
        return Deposit(uid!!, bankCode!!, icon!!, title!!, description!!, maximum!!, minimum!!)
    }
}