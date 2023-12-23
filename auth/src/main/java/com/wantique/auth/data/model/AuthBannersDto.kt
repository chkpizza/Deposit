package com.wantique.auth.data.model

import androidx.annotation.Keep
import com.wantique.auth.domain.model.AuthBanners

@Keep
data class AuthBannersDto(
    val banners: List<AuthBannerDto> = emptyList()
) {
    fun asDomain(): AuthBanners {
        return AuthBanners(banners.map { it.asDomain() })
    }
}