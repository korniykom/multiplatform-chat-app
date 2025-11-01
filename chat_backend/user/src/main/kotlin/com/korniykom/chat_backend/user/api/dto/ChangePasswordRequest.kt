package com.korniykom.chat_backend.user.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.korniykom.chat_backend.user.api.util.Password
import jakarta.validation.constraints.NotBlank

data class ChangePasswordRequest(
    @field:NotBlank
    @JsonProperty("newPassword")
    val newPassword: String,
    @field:NotBlank
    @JsonProperty("oldPassword")
    val oldPassword: String
)
