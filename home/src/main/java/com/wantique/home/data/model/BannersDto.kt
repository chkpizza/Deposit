package com.wantique.home.data.model

import androidx.annotation.Keep
import com.wantique.home.domain.model.Banners

@Keep
data class BannersDto(
    val banners: List<BannerDto>
) {
    fun asDomain(): Banners {
        return Banners(banners.map { it.asDomain() })
    }
}