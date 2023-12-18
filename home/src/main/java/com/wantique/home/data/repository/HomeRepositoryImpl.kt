package com.wantique.home.data.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.wantique.base.network.Resource
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
    /*
    suspend fun registerDeposit() {
        Firebase.firestore.collection("deposit").document("highest").set(
            DepositsDto("은행별 최고 금리 예금 상품", listOf(
                DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "KB Star 정기예금", "2.94", "4.00"),
                DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "NH고향사랑기부예금", "3.10", "3.90"),
                DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "신한 My플러스 정기예금", "3.80", "4.00")
            ))
        ).await()
    }

     */
    override fun getHighestDepositByBank(): Flow<Resource<Deposits>> = flow {
        Firebase.firestore.collection("deposit").document("highest").get().await().toObject<DepositsDto>()?.let {
            emit(Resource.Success(it.asDomain()))
        } ?: emit(Resource.Error(Throwable("HIGHEST_DEPOSIT_NOT_FOUND")))
    }
}