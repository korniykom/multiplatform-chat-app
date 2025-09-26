package com.korniykom.chat_backend.user.infra.database.mappers

import com.korniykom.chat_backend.user.domain.model.User
import com.korniykom.chat_backend.user.infra.database.entity.UserEntity

fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail
    )
}