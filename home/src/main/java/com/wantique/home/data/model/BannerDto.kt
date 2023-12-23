package com.wantique.home.data.model

import androidx.annotation.Keep
import com.wantique.home.domain.model.Banner

@Keep
data class BannerDto(
    val url: String = ""
) {
    fun asDomain(): Banner {
        return Banner(url)
    }
}