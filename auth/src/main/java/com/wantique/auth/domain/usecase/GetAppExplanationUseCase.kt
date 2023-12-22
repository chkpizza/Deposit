package com.wantique.auth.domain.usecase

import com.wantique.auth.domain.repository.AuthRepository
import com.wantique.base.network.Resource
import com.wantique.base.ui.UiState
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAppExplanationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getAppExplanation().map {
        when(it) {
            is Resource.Success -> UiState.Success(it.data.asDomain())
            is Resource.Error -> UiState.Error(it.error)
        }
    }
}