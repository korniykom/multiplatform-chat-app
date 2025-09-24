package org.korniykom.com.korniykom.chat_backend.user.domain.model

data class AuthenticatedUser(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
)
