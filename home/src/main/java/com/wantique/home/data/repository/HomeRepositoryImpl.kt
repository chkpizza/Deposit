package com.wantique.home.data.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.wantique.base.network.Resource
import com.wantique.home.data.model.DepositDto
import com.wantique.home.data.model.DepositsDto
import com.wantique.home.domain.model.Deposits
import com.wantique.home.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : HomeRepository {
    override fun getHighestDepositByBank(): Flow<Resource<Deposits>> = flow {
        Firebase.firestore.collection("deposit").orderBy("maximum", Query.Direction.DESCENDING).limit(5).get().await().toObjects<DepositDto>().run {
            emit(Resource.Success(Deposits("", map { it.asDomain()})))
        }
    }
}