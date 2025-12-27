package com.korniykom.kotlin_chat.infra.mappers

import com.korniykom.kotlin_chat.domain.model.EmailVerificationToken
import com.korniykom.kotlin_chat.infra.database.entities.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}