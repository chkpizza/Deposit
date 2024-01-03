package com.wantique.home.domain.model

data class Saving(
    val uid: String,
    val bankCode: Int,
    val title: String,
    val description: String,
    val maxRate: Double,
    val minRate: Double
)
