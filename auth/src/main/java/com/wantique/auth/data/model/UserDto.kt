package com.wantique.auth.data.model

import androidx.annotation.Keep
import com.wantique.auth.domain.model.User

@Keep
data class UserDto(
    val uid: String = "",
    val date: String = ""
) {
    fun asDomain(): User {
        return User(uid)
    }
}
