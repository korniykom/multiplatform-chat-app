package com.korniykom.chat_backend.user.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.korniykom.chat_backend.user.api.util.Password
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class ResetPasswordRequest(
    @JsonProperty("token")
    @field:NotBlank
    val token: String,
    @JsonProperty("newPassword")
    @field:Password
    val newPassword: String
)
