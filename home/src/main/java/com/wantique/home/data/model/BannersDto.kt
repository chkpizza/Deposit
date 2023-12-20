package com.wantique.home.data.model

import com.wantique.home.domain.model.Banners

data class BannersDto(
    val banners: List<BannerDto>
) {
    fun asDomain(): Banners {
        return Banners(banners.map { it.asDomain() })
    }
}