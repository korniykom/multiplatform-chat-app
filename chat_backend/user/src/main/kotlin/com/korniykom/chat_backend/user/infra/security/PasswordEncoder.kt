package com.korniykom.chat_backend.user.infra.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(rawPassword: String): String = bcrypt.encode(rawPassword)!!
    fun matches(rawPassword: String, hashedPassword: String): Boolean = bcrypt.matches(rawPassword, hashedPassword)
}