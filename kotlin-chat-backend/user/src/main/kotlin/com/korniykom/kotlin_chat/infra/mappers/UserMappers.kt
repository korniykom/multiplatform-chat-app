package com.korniykom.kotlin_chat.infra.mappers

import com.korniykom.kotlin_chat.domain.model.User
import com.korniykom.kotlin_chat.infra.database.entities.UserEntity

fun UserEntity.toUser() : User {
    return User(
        id = id!!,
        username = username,
        email = email,
        hasEmailVerified = hasVerifiedEmail,

    )
}