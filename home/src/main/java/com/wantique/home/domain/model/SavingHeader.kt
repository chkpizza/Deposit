package com.wantique.home.domain.model

data class SavingHeader(
    val uid: String,
    val bankCode: Int,
    val title: String,
    val description: String,
    val maxRate: Double,
    val minRate: Double,
    val rateDescription: String,
    val taxFree: Boolean
)