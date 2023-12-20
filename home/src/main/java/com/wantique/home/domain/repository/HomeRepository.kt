package com.wantique.home.domain.repository

import com.wantique.base.network.Resource
import com.wantique.home.data.model.BannersDto
import com.wantique.home.data.model.DepositHeaderDto
import com.wantique.home.data.model.SummaryDepositsDto
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHighestDepositByBank(): Flow<Resource<SummaryDepositsDto>>
    fun getHomeBanner(): Flow<Resource<BannersDto>>
    fun getAllDepositProduct(): Flow<Resource<SummaryDepositsDto>>
    fun getDepositHeader(uid: String): Flow<Resource<DepositHeaderDto>>
}