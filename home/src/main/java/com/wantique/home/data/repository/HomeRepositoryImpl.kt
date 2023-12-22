package com.wantique.home.data.repository

import android.content.res.Resources.NotFoundException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.wantique.base.exception.DepositNotFoundException
import com.wantique.base.exception.EmptyListException
import com.wantique.base.network.Resource
import com.wantique.home.data.model.BannerDto
import com.wantique.home.data.model.BannersDto
import com.wantique.home.data.model.DepositBodyDto
import com.wantique.home.data.model.DepositHeaderDto
import com.wantique.home.data.model.SummaryDepositDto
import com.wantique.home.data.model.SummaryDepositsDto
import com.wantique.home.domain.repository.HomeRepository
import com.wantique.home.ui.detail.model.DepositHeader
import com.wantique.resource.Constant
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
        Firebase.firestore.collection("image").document("banner").collection("home").document(System.currentTimeMillis().toString()).set(
            BannerDto("https://image.xportsnews.com/contents/images/upload/article/2023/0310/mb_1678433961946948.jpg")
        ).await()
        */

        /*
        Firebase.firestore.collection("bank").document("deposit").collection("summary").document("05141dafce2a8535a5558caf88135ef40f57168d7a8d299da9cdb842f8cee217").set(
            SummaryDepositDto("05141dafce2a8535a5558caf88135ef40f57168d7a8d299da9cdb842f8cee217", 1, "NH고향사랑기부예금", "고향사랑기부제 참여 시 우대금리를 제공하고 공익기금을 적립하는 지역사회공헌 상품", 3.10, 3.90)
        ).await()
        */

        /*
        Firebase.firestore.collection("bank").document("deposit").collection("header").document("05141dafce2a8535a5558caf88135ef40f57168d7a8d299da9cdb842f8cee217").set(
            DepositHeaderDto("05141dafce2a8535a5558caf88135ef40f57168d7a8d299da9cdb842f8cee217", 1, "NH고향사랑기부예금", "고향사랑기부제 참여 시 우대금리를 제공하고 공익기금을 적립하는 지역사회공헌 상품", 3.10, 3.90, "세전", true)
        ).await()
        */

        /*
        Firebase.firestore.collection("bank").document("deposit").collection("body").document("05141dafce2a8535a5558caf88135ef40f57168d7a8d299da9cdb842f8cee217").set(
            DepositBodyDto("05141dafce2a8535a5558caf88135ef40f57168d7a8d299da9cdb842f8cee217", 1, "영업점 및 비대면", "개인", "1년(12개월)", "1백만원 이상", true)
        ).await()
        */
    }

    override fun getHighestDepositByBank(): Flow<Resource<SummaryDepositsDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.SUMMARY_COLLECTION).orderBy("maxRate", Query.Direction.DESCENDING).limit(5).get().await().toObjects<SummaryDepositDto>().run {
            if(isEmpty()) {
                emit(Resource.Error(EmptyListException()))
            } else {
                emit(Resource.Success(SummaryDepositsDto("최고 금리 TOP5", this)))
            }
        }
    }

    override fun getHomeBanner(): Flow<Resource<BannersDto>> = flow {
        Firebase.firestore.collection(Constant.IMAGE_COLLECTION).document(Constant.BANNER_DOCUMENT).collection(Constant.HOME_COLLECTION).get().await().toObjects<BannerDto>().run {
            if(isEmpty()) {
                emit(Resource.Error(EmptyListException()))
            } else {
                emit(Resource.Success(BannersDto(this)))
            }
        }
    }

    override fun getAllDepositProduct(): Flow<Resource<SummaryDepositsDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.SUMMARY_COLLECTION).get().await().toObjects<SummaryDepositDto>().run {
            if(isEmpty()) {
                emit(Resource.Error(EmptyListException()))
            } else {
                emit(Resource.Success(SummaryDepositsDto("등록된 예금 상품", this)))
            }
        }
    }

    override fun getDepositHeader(uid: String): Flow<Resource<DepositHeaderDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.HEADER_COLLECTION).document(uid).get().await().toObject<DepositHeaderDto>()?.let {
            emit(Resource.Success(it))
        } ?: emit(Resource.Error(DepositNotFoundException()))
    }

    override fun getDepositBody(uid: String): Flow<Resource<DepositBodyDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.BODY_COLLECTION).document(uid).get().await().toObject<DepositBodyDto>()?.let {
            emit(Resource.Success(it))
        } ?: emit(Resource.Error(DepositNotFoundException()))
    }
}