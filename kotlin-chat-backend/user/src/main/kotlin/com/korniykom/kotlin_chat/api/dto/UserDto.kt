package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.UserId

data class UserDto(
    val id: UserId,
    val email: String,
    val hasVerifiedEmail: Boolean,
    val username: String
)
