package com.wantique.auth.data.model

import androidx.annotation.Keep
import com.wantique.auth.domain.model.AuthBanner

@Keep
data class AuthBannerDto(
    val uid: String? = null,
    val url: String? = null
) {
    fun asDomain(): AuthBanner {
        return AuthBanner(uid!!, url!!)
    }
}