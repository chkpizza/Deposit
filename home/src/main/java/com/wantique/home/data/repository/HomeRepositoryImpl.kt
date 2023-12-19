package com.wantique.home.data.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.wantique.base.network.Resource
import com.wantique.home.data.model.BannerDto
import com.wantique.home.data.model.BannersDto
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
    suspend fun registerDeposit() {
        /*
        Firebase.firestore.collection("deposit").document("highest").set(
            DepositsDto("은행별 최고 금리 예금 상품", listOf(
                DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "KB Star 정기예금", "2.94", "4.00"),
                DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "NH고향사랑기부예금", "3.10", "3.90"),
                DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "신한 My플러스 정기예금", "3.80", "4.00")
            ))
        ).await()

         */

        /*
        Firebase.firestore.collection("deposit").document(System.currentTimeMillis().toString()).set(
            DepositDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg", "NH고향사랑기부예금", "3.90", "3.10"),
        ).await()
        */

        /*
        Firebase.firestore.collection("banner").document(System.currentTimeMillis().toString()).set(
            BannerDto("https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2022/05/22/f1c66ccb-f5bf-4382-af54-96ba8f2d3fb5.jpg")
        ).await()
         */
    }

    override fun getHighestDepositByBank(): Flow<Resource<Deposits>> = flow {
        Firebase.firestore.collection("deposit").orderBy("maximum", Query.Direction.DESCENDING).limit(5).get().await().toObjects<DepositDto>().run {
            emit(Resource.Success(Deposits("최고 금리 TOP 5", map { it.asDomain()})))
        }
    }

    override fun getHomeBanner(): Flow<Resource<BannersDto>> = flow {
        Firebase.firestore.collection("banner").get().await().toObjects<BannerDto>().run {
            if(isEmpty()) {
                emit(Resource.Error(Throwable("EMPTY_BANNER")))
            } else {
                emit(Resource.Success(BannersDto(this)))
            }
        }
    }
}