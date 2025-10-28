package com.korniykom.chat_backend.user.api.controllers

import com.korniykom.chat_backend.user.api.dto.*
import com.korniykom.chat_backend.user.api.mappers.toAuthenticatedUserDto
import com.korniykom.chat_backend.user.api.mappers.toUserDto
import com.korniykom.chat_backend.user.infra.rate_limiting.EmailRateLimiter
import com.korniykom.chat_backend.user.service.AuthService
import com.korniykom.chat_backend.user.service.EmailVerificationService
import com.korniykom.chat_backend.user.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService,
    private val emailRateLimiter: EmailRateLimiter
) {

    @PostMapping("/register")
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ) : UserDto {
        return authService.register(
            email = body.email,
            username = body.username,
            password = body.password
        ).toUserDto()
    }

    @PostMapping("login")
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toAuthenticatedUserDto()
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest
    ) : AuthenticatedUserDto {
        return authService
            .refresh(body.refreshToken)
            .toAuthenticatedUserDto()
    }

    @GetMapping("/verify")
    fun verifyEmail(
        @RequestParam token: String
    ) {
        emailVerificationService.verifyEmail(token)
    }

    @PostMapping("/reset-password")
    fun resetPassword(
        @Valid @RequestBody body: ResetPasswordRequest
    ) {
        passwordResetService.resetPassword(body.token, body.newPassword)
    }

    @PostMapping("change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
       // TODO extract userid and call service
    }

    @PostMapping("forgot-password")
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ) {
        passwordResetService.requestPasswordReset(body.email)
    }

    @PostMapping("resend-verification")
    fun resendVerification(
        @Valid @RequestBody body: EmailRequest

    ) {
        emailRateLimiter.withRateLimit(email = body.email) {
            emailVerificationService.resendVerificationEmail(body.email)
        }
    }
}