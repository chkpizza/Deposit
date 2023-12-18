package com.wantique.home.domain.model

import com.wantique.home.ui.model.Deposit

data class Deposit(
    val icon: String,
    val title: String,
    val maximum: String,
    val minimum: String,
) {
    fun asUi(): Deposit {
        return Deposit(icon, title, maximum, minimum)
    }
}