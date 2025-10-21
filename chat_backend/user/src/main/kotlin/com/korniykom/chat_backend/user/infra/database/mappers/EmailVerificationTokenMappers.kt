package com.korniykom.chat_backend.user.infra.database.mappers

import com.korniykom.chat_backend.user.domain.model.EmailVerificationToken
import com.korniykom.chat_backend.user.infra.database.entity.EmailVerificationTokenEntity

fun EmailVerificationTokenEntity.toEmailVerificationToken(): EmailVerificationToken {
    return EmailVerificationToken(
        id = id,
        token = token,
        user = user.toUser()
    )
}