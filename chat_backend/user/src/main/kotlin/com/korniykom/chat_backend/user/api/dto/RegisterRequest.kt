package com.korniykom.chat_backend.user.api.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length
import jakarta.validation.constraints.Pattern

data class RegisterRequest @JsonCreator constructor(
    @field:Email(message = "Must be a valid email address")
    @JsonProperty("email")
    val email: String,
    @field:Length(min = 3, max = 20, message = "Username length must be between 3 and 20")
    @JsonProperty("username")
    val username: String,
    @field:Pattern(
        regexp = "^(?=.*[\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(.{8,})$",
        message = "Password must be at least 8 characters and contain at least one digit or special character"
    )
    @JsonProperty("password")
    val password: String
)
