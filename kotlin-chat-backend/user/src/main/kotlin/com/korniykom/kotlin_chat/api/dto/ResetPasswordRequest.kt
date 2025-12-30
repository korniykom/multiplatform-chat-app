package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.api.util.Password
import jakarta.validation.constraints.NotBlank

class ResetPasswordRequest(
    @field:NotBlank
    val token: String,
    @field:Password
    val newPassword: String
) {
}