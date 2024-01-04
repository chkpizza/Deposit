package com.wantique.home.data.model

import com.wantique.home.domain.model.SavingBody

data class SavingBodyDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val signUpMethod: String? = null,
    val target: String? = null,
    val contractPeriod: String? = null,
    val signUpAmount: String? = null,
    val protect: Boolean? = null,
    val url: String? = null
) {
    fun asDomain(): SavingBody {
        return SavingBody(uid!!, bankCode!!, signUpMethod!!, target!!, contractPeriod!!, signUpAmount!!, protect!!, url!!)
    }
}