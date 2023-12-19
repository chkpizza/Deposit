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
        Firebase.firestore.collection("banner").document(System.currentTimeMillis().toString()).set(
            BannerDto("https://talkimg.imbc.com/TVianUpload/tvian/TViews/image/2022/05/22/f1c66ccb-f5bf-4382-af54-96ba8f2d3fb5.jpg")
        ).await()
         */

        Firebase.firestore.collection("bank").document("deposit").collection("summary").document("d19b5ea057b17a5b4f6673706203b29a5929ffa635abc67a913030d75069ae66").set(
            DepositDto(3, "", "신한 My플러스 정기예금", "요건 달성 시 우대이자율을 제공하는 정기예금", 4.00, 3.80)
        ).await()
    }

    override fun getHighestDepositByBank(): Flow<Resource<DepositsDto>> = flow {
        Firebase.firestore.collection("bank").document("deposit").collection("summary").orderBy("maximum", Query.Direction.DESCENDING).limit(5).get().await().toObjects<DepositDto>().run {
            if(isEmpty()) {
                emit(Resource.Error(Throwable("EMPTY_PRODUCT")))
            } else {
                emit(Resource.Success(DepositsDto("최고 금리 TOP5", this)))
            }
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