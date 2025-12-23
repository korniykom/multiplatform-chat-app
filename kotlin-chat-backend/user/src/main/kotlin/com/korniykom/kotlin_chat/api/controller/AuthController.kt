package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.dto.RegisterRequest
import com.korniykom.kotlin_chat.api.dto.UserDto
import com.korniykom.kotlin_chat.api.mappers.toUserDto
import com.korniykom.kotlin_chat.service.auth.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            username = body.username,
            email = body.username,
            password = body.password
        ).toUserDto()
    }
}