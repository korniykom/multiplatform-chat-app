package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.api.util.Password
import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class RegisterRequest(
    @field:Email("Must be a valid email")
    val email: String,
    @field:Length(min = 3, max = 20, message = "Username length must be between 3 and 20")
    val username: String,
    @field:Password
    val password: String
)
