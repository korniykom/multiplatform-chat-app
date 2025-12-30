package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.config.IpRateLimit
import com.korniykom.kotlin_chat.api.dto.AuthenticatedUserDto
import com.korniykom.kotlin_chat.api.dto.ChangePasswordRequest
import com.korniykom.kotlin_chat.api.dto.EmailRequest
import com.korniykom.kotlin_chat.api.dto.LoginRequest
import com.korniykom.kotlin_chat.api.dto.RefreshRequest
import com.korniykom.kotlin_chat.api.dto.RegisterRequest
import com.korniykom.kotlin_chat.api.dto.ResetPasswordRequest
import com.korniykom.kotlin_chat.api.dto.UserDto
import com.korniykom.kotlin_chat.api.mappers.toAuthenticatedUserDto
import com.korniykom.kotlin_chat.api.mappers.toUserDto
import com.korniykom.kotlin_chat.infra.rate_limiting.EmailRateLimiter
import com.korniykom.kotlin_chat.service.AuthService
import com.korniykom.kotlin_chat.service.EmailVerificationService
import com.korniykom.kotlin_chat.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val emailVerificationService: EmailVerificationService,
    private val passwordRequestService: PasswordResetService,
    private val passwordResetService: PasswordResetService,
    private val emailRateLimiter: EmailRateLimiter
) {
    @PostMapping("/register")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun register(
        @Valid @RequestBody body: RegisterRequest
    ): UserDto {
        return authService.register(
            username = body.username,
            email = body.email,
            password = body.password
        ).toUserDto()
    }

    @PostMapping("/login")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun login(
        @RequestBody
        body: LoginRequest
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
        timeUnit = TimeUnit.HOURS
    )
    fun refresh(
        @RequestBody
        body: RefreshRequest
    ): AuthenticatedUserDto {
        return authService
            .refresh(body.refreshToken)
            .toAuthenticatedUserDto()
    }

    @PostMapping("/logout")
    fun logout(
        @RequestBody
        body: RefreshRequest
    ) {
        authService.logout(body.refreshToken)

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
        passwordRequestService.resetPassword(newPassword = body.newPassword, token = body.token)
    }

    @PostMapping("/forgot-password")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun forgotPassword(
        @Valid @RequestBody body: EmailRequest
    ) {
        passwordResetService.requestPasswordReset(body.email)
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody body: ChangePasswordRequest
    ) {
        // TODO Extract user ID and call service
    }


    @PostMapping("/resend-verification")
    @IpRateLimit(
        requests = 10,
        duration = 1L,
        timeUnit = TimeUnit.HOURS
    )
    fun resendVerification(
        @Valid @RequestBody body: EmailRequest
    ) {
        emailRateLimiter.withRateLimit(body.email) {
            emailVerificationService.resendVerificationEmail(body.email)
        }
    }
}