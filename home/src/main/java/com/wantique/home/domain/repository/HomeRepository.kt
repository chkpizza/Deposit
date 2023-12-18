package com.wantique.home.domain.repository

import com.wantique.base.network.Resource
import com.wantique.home.domain.model.Deposits
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHighestDepositByBank(): Flow<Resource<Deposits>>
}