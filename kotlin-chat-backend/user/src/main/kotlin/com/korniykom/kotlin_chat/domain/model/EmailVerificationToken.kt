package com.korniykom.kotlin_chat.domain.model

data class EmailVerificationToken(
    val id: Long,
    val token: String,
    val user: User
)