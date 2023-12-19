package com.wantique.home.domain.model

data class Deposit(
    val bankCode: Int,
    val icon: String,
    val title: String,
    val description: String,
    val maximum: Double,
    val minimum: Double,
)