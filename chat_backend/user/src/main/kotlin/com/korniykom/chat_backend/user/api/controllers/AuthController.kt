package com.korniykom.chat_backend.user.api.controllers

import com.korniykom.chat_backend.user.api.config.IpRateLimit
import com.korniykom.chat_backend.user.api.dto.*
import com.korniykom.chat_backend.user.api.mappers.toAuthenticatedUserDto
import com.korniykom.chat_backend.user.api.mappers.toUserDto
import com.korniykom.chat_backend.user.api.util.requestUserId
import com.korniykom.chat_backend.user.domain.model.UserId
import com.korniykom.chat_backend.user.infra.rate_limiting.EmailRateLimiter
import com.korniykom.chat_backend.user.service.AuthService
import com.korniykom.chat_backend.user.service.EmailVerificationService
import com.korniykom.chat_backend.user.service.JwtService
import com.korniykom.chat_backend.user.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtService: JwtService,
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordResetService: PasswordResetService,
    private val emailRateLimiter: EmailRateLimiter
) {

    @PostMapping("/register")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
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
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun login(
        @RequestBody body: LoginRequest
    ): AuthenticatedUserDto {
        return authService.login(
            email = body.email,
            password = body.password
        ).toAuthenticatedUserDto()
    }

    @PostMapping("/refresh")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
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
        @RequestHeader("Authorization") authorizationHeader: String,
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
        val token = authorizationHeader.removePrefix("Bearer ").trim()
        val userId = jwtService.getUserIdFromToken(token)

        passwordResetService.changePassword(
            userId = userId,
            oldPassword = body.oldPassword,
            newPassword = body.newPassword
        )
    }

    @PostMapping("forgot-password")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ) {
        passwordResetService.requestPasswordReset(body.email)
    }

    @PostMapping("resend-verification")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        unit = TimeUnit.HOURS
    )
    fun resendVerification(
        @Valid @RequestBody body: EmailRequest

    ) {
        emailRateLimiter.withRateLimit(email = body.email) {
            emailVerificationService.resendVerificationEmail(body.email)
        }
    }
}
