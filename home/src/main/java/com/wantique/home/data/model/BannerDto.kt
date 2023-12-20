package com.wantique.home.data.model

import com.wantique.home.domain.model.Banner

data class BannerDto(
    val url: String = ""
) {
    fun asDomain(): Banner {
        return Banner(url)
    }
}