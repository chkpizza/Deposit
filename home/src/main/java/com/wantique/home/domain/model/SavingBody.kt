package com.wantique.home.domain.model

data class SavingBody(
    val uid: String,
    val bankCode: Int,
    val signUpMethod: String,
    val target: String,
    val contractPeriod: String,
    val signUpAmount: String,
    val protect: Boolean,
    val url: String
)