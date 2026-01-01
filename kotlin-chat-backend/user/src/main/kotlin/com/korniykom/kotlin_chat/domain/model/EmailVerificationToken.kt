package com.korniykom.kotlin_chat.domain.model

import com.korniykom.kotlin_chat.type.User

data class EmailVerificationToken(
    val id: Long,
    val token: String,
    val user: User
)