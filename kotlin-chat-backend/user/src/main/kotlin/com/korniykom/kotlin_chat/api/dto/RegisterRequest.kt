package com.korniykom.kotlin_chat.api.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern
import org.hibernate.validator.constraints.Length
import org.springframework.stereotype.Component

data class RegisterRequest(
    @field:Email("Must be a valid email")
    val email: String,
    @field:Length(min = 3, max = 20, message = "Username length must be between 3 and 20")
    val username: String,
    @field:Pattern(
        regexp = "^(?=.*[\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])(.{8,})$",
        message = "Password must be at least 8 characters and contain at least one digit or special character"
    )
    val password: String
)
