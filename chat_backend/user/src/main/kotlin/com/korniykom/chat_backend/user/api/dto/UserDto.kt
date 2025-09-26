package com.korniykom.chat_backend.user.api.dto

import com.korniykom.chat_backend.user.domain.model.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean
)
