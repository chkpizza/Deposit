package com.wantique.auth.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.wantique.auth.domain.usecase.IsExistUserUseCase
import com.wantique.auth.domain.usecase.RegisterUserUseCase
import com.wantique.base.exception.UserNotFoundException
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.getValue
import com.wantique.base.ui.isErrorOrNull
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val isExistUserUseCase: IsExistUserUseCase,
    private val registerUserUseCase: RegisterUserUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _navigateToHome = MutableSharedFlow<Unit>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    fun isValidUser() {
        viewModelScope.launch {
            safeFlow {
                isExistUserUseCase()
            }.onEach {
                it.isErrorOrNull()?.let { e ->
                    if(e is UserNotFoundException) {
                        registerUser()
                    }
                } ?: run {
                    _navigateToHome.emit(Unit)
                }
            }.collect()
        }
    }

    private suspend fun registerUser() {
        safeFlow {
            registerUserUseCase()
        }.onEach {
            it.isErrorOrNull()?.let { e ->
                if(e is UserNotFoundException) {
                    _errorState.emit(e)
                }
            } ?: run {
                _navigateToHome.emit(Unit)
            }
        }.collect()
    }
}