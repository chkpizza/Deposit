package com.wantique.home.data.repository

import android.util.Log
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
import com.wantique.home.data.model.DepositDto
import com.wantique.home.data.model.DepositHeaderDto
import com.wantique.home.data.model.DepositsDto
import com.wantique.home.data.model.SavingDto
import com.wantique.home.data.model.SavingsDto
import com.wantique.home.data.model.TitleDto
import com.wantique.home.domain.repository.HomeRepository
import com.wantique.resource.Constant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : HomeRepository {
    override fun getHighestDepositByBank(): Flow<Resource<DepositsDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_TOP_TITLE_DOCUMENT).get().await().toObject<TitleDto>()?.let { titleDto ->
            Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.SUMMARY_COLLECTION).orderBy("maxRate", Query.Direction.DESCENDING).limit(5).get().await().toObjects<DepositDto>().run {
                if(isEmpty()) {
                    emit(Resource.Error(EmptyListException()))
                } else {
                    emit(Resource.Success(DepositsDto(titleDto.title!!, this)))
                }
            }
        } ?: emit(Resource.Error(EmptyListException()))
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

    override fun getAllDepositProduct(): Flow<Resource<DepositsDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_TITLE_DOCUMENT).get().await().toObject<TitleDto>()?.let { titleDto ->
            Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.SUMMARY_COLLECTION).get().await().toObjects<DepositDto>().run {
                if(isEmpty()) {
                    emit(Resource.Error(EmptyListException()))
                } else {
                    emit(Resource.Success(DepositsDto(titleDto.title!!, this)))
                }
            }
        } ?: emit(Resource.Error(EmptyListException()))
    }

    override fun getDepositProduct(): Flow<Resource<DepositsDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_TITLE_DOCUMENT).get().await().toObject<TitleDto>()?.let { titleDto ->
            Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.SUMMARY_COLLECTION).get().await().toObjects<DepositDto>().run {
                if(isEmpty()) {
                    emit(Resource.Error(EmptyListException()))
                } else {
                    emit(Resource.Success(DepositsDto(titleDto.title!!, shuffled().take(4))))
                }
            }
        } ?: emit(Resource.Error(EmptyListException()))
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

    override fun getSavingProduct(): Flow<Resource<SavingsDto>> = flow {
        Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.SAVING_TITLE_DOCUMENT).get().await().toObject<TitleDto>()?.let { titleDto ->
            Firebase.firestore.collection(Constant.BANK_COLLECTION).document(Constant.DEPOSIT_DOCUMENT).collection(Constant.SUMMARY_COLLECTION).get().await().toObjects<SavingDto>().run {
                if(isEmpty()) {
                    emit(Resource.Error(EmptyListException()))
                } else {
                    emit(Resource.Success(SavingsDto(titleDto.title!!, shuffled().take(6))))
                }
            }
        } ?: emit(Resource.Error(EmptyListException()))
    }
}