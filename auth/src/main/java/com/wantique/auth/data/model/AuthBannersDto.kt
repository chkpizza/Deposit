package com.wantique.auth.data.model

import com.wantique.auth.domain.model.AuthBanners

data class AuthBannersDto(
    val banners: List<AuthBannerDto> = emptyList()
) {
    fun asDomain(): AuthBanners {
        return AuthBanners(banners.map { it.asDomain() })
    }
}