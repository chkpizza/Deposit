package com.wantique.home.data.model

import androidx.annotation.Keep
import com.wantique.home.domain.model.DepositBody

@Keep
data class DepositBodyDto(
    val uid: String? = null,
    val bankCode: Int? = null,
    val signUpMethod: String? = null,
    val target: String? = null,
    val contractPeriod: String? = null,
    val signUpAmount: String? = null,
    val protect: Boolean? = null,
    val url: String? = null
) {
    fun asDomain(): DepositBody {
        return DepositBody(uid!!, bankCode!!, signUpMethod!!, target!!, contractPeriod!!, signUpAmount!!, protect!!, url!!)
    }
}