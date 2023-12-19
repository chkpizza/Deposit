package com.wantique.base.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wantique.base.network.NetworkState
import com.wantique.base.network.NetworkStateTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

open class BaseViewModel(
    private val networkStateTracker: NetworkStateTracker,
    private val context: Context
) : ViewModel() {
    protected val _errorState = MutableStateFlow<Throwable?>(null)
    val errorState = _errorState.asStateFlow()

    private lateinit var networkState: NetworkState

    init {
        initializeNetworkState()
        trackingNetworkState()
    }

    private fun trackingNetworkState() {
        viewModelScope.launch(Dispatchers.IO) {
            networkStateTracker.networkStatus
                .onEach {
                    networkState = it
                }.collect()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initializeNetworkState() {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
            networkState = if(it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or it.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR)) {
                NetworkState.Available
            } else {
                NetworkState.UnAvailable
            }
        } ?: run {
            networkState = NetworkState.UnAvailable
        }
    }

    protected fun isAvailableNetwork() = when(networkState) {
        is NetworkState.Available -> true
        is NetworkState.UnAvailable -> false
    }

    fun <T> safeFlow(executor: () -> Flow<UiState<T>>): Flow<UiState<T>> = flow {
        if(isAvailableNetwork()) {
            _errorState.value = null
            emitAll(executor())
        } else {
            emit(UiState.Error(Throwable("NETWORK_CONNECTION_ERROR")))
        }
    }
}