package com.wantique.home.domain.usecase

import com.wantique.base.network.Resource
import com.wantique.base.ui.UiState
import com.wantique.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSavingProductUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke() = homeRepository.getSavingProduct().map {
        when(it) {
            is Resource.Success -> UiState.Success(it.data.asDomain())
            is Resource.Error -> UiState.Error(it.error)
        }
    }
}