package com.korniykom.chat_backend.user.api.dto

import com.korniykom.chat_backend.user.api.util.Password
import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:Password
    val newPassword: String,
    @field:NotBlank
    val oldPassword: String
)
