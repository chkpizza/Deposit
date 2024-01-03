package com.wantique.home.data.model

import com.wantique.home.domain.model.Savings

data class SavingsDto(
    val title: String? = null,
    val savings: List<SavingDto> = emptyList()
) {
    fun asDomain(): Savings {
        return Savings(title!!, savings.map { it.asDomain() })
    }
}