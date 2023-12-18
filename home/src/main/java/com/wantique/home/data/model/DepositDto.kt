package com.wantique.home.data.model

import com.wantique.home.domain.model.Deposit

data class DepositDto(
    val icon: String = "",
    val title: String = "",
    val maximum: String = "",
    val minimum: String = "",
) {
    fun asDomain(): Deposit {
        return Deposit(icon, title, maximum, minimum)
    }
}