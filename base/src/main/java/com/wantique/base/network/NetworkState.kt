package com.wantique.base.network

sealed class NetworkState {
    data object Available : NetworkState()
    data object UnAvailable : NetworkState()
}