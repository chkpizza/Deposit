package com.wantique.home.ui.detail

import android.content.Context
import com.wantique.base.network.NetworkStateTracker
import com.wantique.base.ui.BaseViewModel
import javax.inject.Inject

class DepositViewModel @Inject constructor(
    networkStateTracker: NetworkStateTracker,
    context: Context
) : BaseViewModel(networkStateTracker, context) {

}