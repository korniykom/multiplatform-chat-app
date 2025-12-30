package com.korniykom.kotlin_chat.api.dto
import com.korniykom.kotlin_chat.api.util.Password
import com.korniykom.kotlin_chat.domain.model.UserId
import jakarta.validation.constraints.NotBlank

class ChangePasswordRequest(
    @field:Password
    val newPassword: String,
    @field:Password
    val oldPassword: String
) {
}