package com.wantique.auth.data.model

import com.wantique.auth.domain.model.User

data class UserDto(
    val uid: String = "",
    val date: String = ""
) {
    fun asDomain(): User {
        return User(uid)
    }
}
