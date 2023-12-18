package com.wantique.auth.domain.repository

import com.wantique.auth.domain.model.User
import com.wantique.base.network.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isExistUser(): Flow<Resource<User>>
}