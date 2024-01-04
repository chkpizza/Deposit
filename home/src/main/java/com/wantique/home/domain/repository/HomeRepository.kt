package com.wantique.home.domain.repository

import com.wantique.base.network.Resource
import com.wantique.home.data.model.BannersDto
import com.wantique.home.data.model.DepositBodyDto
import com.wantique.home.data.model.DepositHeaderDto
import com.wantique.home.data.model.DepositsDto
import com.wantique.home.data.model.SavingsDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHighestDepositByBank(): Flow<Resource<DepositsDto>>
    fun getHomeBanner(): Flow<Resource<BannersDto>>
    fun getAllDepositProduct(): Flow<Resource<DepositsDto>>
    fun getShuffledDepositProduct(): Flow<Resource<DepositsDto>>
    fun getDepositHeader(uid: String): Flow<Resource<DepositHeaderDto>>
    fun getDepositBody(uid: String): Flow<Resource<DepositBodyDto>>
    fun getShuffledSavingProduct(): Flow<Resource<SavingsDto>>
}