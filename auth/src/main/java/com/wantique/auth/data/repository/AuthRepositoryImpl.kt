package com.wantique.auth.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.wantique.auth.data.model.AuthBannerDto
import com.wantique.auth.data.model.AuthBannersDto
import com.wantique.auth.data.model.UserDto
import com.wantique.auth.domain.model.User
import com.wantique.auth.domain.repository.AuthRepository
import com.wantique.base.exception.EmptyListException
import com.wantique.base.exception.UserNotFoundException
import com.wantique.base.network.Resource
import com.wantique.resource.Constant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : AuthRepository {
    override fun isExistUser(): Flow<Resource<User>> = flow {
        Firebase.firestore.collection(Constant.USER_COLLECTION).document(Firebase.auth.uid.toString()).get().await().toObject<UserDto>()?.let {
            emit(Resource.Success(it.asDomain()))
        } ?: run {
            emit(Resource.Error(UserNotFoundException()))
        }
    }

    override fun registerUser(): Flow<Resource<User>> = flow {
        Firebase.firestore.collection(Constant.USER_COLLECTION).document(Firebase.auth.uid.toString()).set(UserDto(Firebase.auth.uid.toString(), System.currentTimeMillis().toString())).await()
        Firebase.firestore.collection(Constant.USER_COLLECTION).document(Firebase.auth.uid.toString()).get().await().toObject<UserDto>()?.let {
            emit(Resource.Success(it.asDomain()))
        } ?: run {
            emit(Resource.Error(UserNotFoundException()))
        }
    }

    override fun getAppExplanation(): Flow<Resource<AuthBannersDto>> = flow {
        Firebase.firestore.collection(Constant.IMAGE_COLLECTION).document(Constant.BANNER_DOCUMENT).collection(Constant.AUTH_COLLECTION).get().await().toObjects<AuthBannerDto>().run {
            if(isEmpty()) {
                emit(Resource.Error(EmptyListException()))
            } else {
                emit(Resource.Success(AuthBannersDto(this)))
            }
        }
    }
}