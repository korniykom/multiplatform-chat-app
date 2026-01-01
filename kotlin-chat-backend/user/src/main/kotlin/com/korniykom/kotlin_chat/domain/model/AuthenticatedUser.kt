package com.korniykom.kotlin_chat.domain.model

import com.korniykom.kotlin_chat.type.User

data class AuthenticatedUser(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)
