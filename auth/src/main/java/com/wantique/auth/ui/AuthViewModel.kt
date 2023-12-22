package com.wantique.auth.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.wantique.auth.domain.usecase.GetAppExplanationUseCase
import com.wantique.auth.domain.usecase.IsExistUserUseCase
import com.wantique.auth.domain.usecase.RegisterUserUseCase
import com.wantique.auth.ui.model.AuthBanner
import com.wantique.auth.ui.model.AuthBanners
import com.wantique.base.exception.UserNotFoundException
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import com.wantique.base.ui.SimpleModel
import com.wantique.base.ui.SimpleSubmittableState
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
    private val getAppExplanationUseCase: GetAppExplanationUseCase,
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {
    private val _navigateToHome = MutableSharedFlow<Unit>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    private val _auth =  MutableStateFlow<SimpleSubmittableState<SimpleModel>?>(null)
    val auth = _auth.asStateFlow()

    private lateinit var banners: AuthBanners<SimpleModel>

    fun load() {
        if(isInitialized()) {
            return
        }
        viewModelScope.launch {
            getAppExplanation()
        }
    }

    private fun isInitialized() = auth.value != null

    private suspend fun getAppExplanation() {
        safeFlow {
            getAppExplanationUseCase()
        }.onEach {
            it.isErrorOrNull()?.let {

            } ?: run {
                banners = AuthBanners(SimpleSubmittableState<SimpleModel>().apply {
                    submitList(it.getValue().banners.map { banner -> AuthBanner(banner.uid, banner.url) })
                })
                merge(banners)
            }
        }.collect()
    }

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

    private fun merge(model: SimpleModel) {
        _auth.value?.let {
            it.submitList(it.getCurrentList().toMutableList().apply {
                add(model)
            })
        } ?: run {
            _auth.value = SimpleSubmittableState<SimpleModel>().apply {
                submitList(listOf(model))
            }
        }
    }
}