package com.korniykom.chat_backend.user.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email

data class EmailRequest(
    @JsonProperty("email")
    @field:Email
    val email: String
)
