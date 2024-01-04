package com.wantique.home.data.model

data class SavingHeaderDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val maxRate: Double? = null,
    val minRate: Double? = null,
    val rateDescription: String? = null,
    val taxFree: Boolean? = null,
)