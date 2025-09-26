package com.korniykom.chat_backend.user.api.dto

data class LoginRequest(
    val email: String,
    val password: String
)
