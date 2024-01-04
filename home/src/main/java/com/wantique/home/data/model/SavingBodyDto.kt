package com.wantique.home.data.model

data class SavingBodyDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val signUpMethod: String? = null,
    val target: String? = null,
    val contractPeriod: String? = null,
    val signUpAmount: String? = null,
    val protect: Boolean? = null,
    val url: String? = null
)