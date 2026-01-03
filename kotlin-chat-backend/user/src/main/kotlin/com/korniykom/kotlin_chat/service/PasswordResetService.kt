package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.api.util.requestUserId
import com.korniykom.kotlin_chat.domain.events.user.UserEvent
import com.korniykom.kotlin_chat.domain.exception.InvalidCredentialException
import com.korniykom.kotlin_chat.domain.exception.InvalidTokenException
import com.korniykom.kotlin_chat.domain.exception.SamePasswordException
import com.korniykom.kotlin_chat.domain.exception.UserNotFoundException
import com.korniykom.kotlin_chat.type.UserId
import com.korniykom.kotlin_chat.infra.database.entities.PasswordResetTokenEntity
import com.korniykom.kotlin_chat.infra.database.repositories.PasswordResetTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.RefreshTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
import com.korniykom.kotlin_chat.infra.message_queue.EventPublisher
import com.korniykom.kotlin_chat.infra.security.PasswordEncoder
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class PasswordResetService(
    private val userRepository: UserRepository,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val eventPublisher: EventPublisher,
    @param:Value("\${email.reset-password.expiry-minutes}") private val expiryMinutes: Long
) {
    @Transactional
    fun requestPasswordReset(email: String) {
        val user = userRepository.findByEmail(email)
            ?: return

        passwordResetTokenRepository.invalidateActiveTokensForUser(user)

        val token = PasswordResetTokenEntity(
            user = user,
            expiresAt = Instant.now().plus(expiryMinutes, ChronoUnit.MINUTES)
        )
        passwordResetTokenRepository.save(token)

        eventPublisher.publish(
            UserEvent.RequestResetPassword(
                userId = user.id!!,
                email = user.email,
                userName = user.username,
                passwordResetToken = token.token,
                expiresInMinutes = expiryMinutes
            )
        )
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String) {
        val resetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if (resetToken.isUsed) {
            throw InvalidTokenException("Reset token is already used")
        }

        if (resetToken.isExpired) {
            throw InvalidTokenException("Reset token is expired")
        }

        val user = resetToken.user

        if (passwordEncoder.matches(newPassword, user.hashedPassword)) {
            throw SamePasswordException()
        }

        val hashedNewPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                hashedPassword = hashedNewPassword
            }
        )

        passwordResetTokenRepository.save(
            resetToken.apply {
                this.usedAt = Instant.now()
            }
        )

        refreshTokenRepository.deleteByUserId(user.id!!)
    }

    @Transactional
    fun changePassword(
        oldPassword: String,
        newPassword: String,
        userId: UserId
    ) {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        if (!passwordEncoder.matches(oldPassword, user.hashedPassword)) {
            throw InvalidCredentialException()
        }

        if (oldPassword == newPassword) {
            throw SamePasswordException()
        }

        refreshTokenRepository.deleteByUserId(user.id!!)

        val newHashedPassword = passwordEncoder.encode(newPassword)
        userRepository.save(
            user.apply {
                hashedPassword = newHashedPassword
            }
        )


    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteByExpiresAtLessThan(
            date = Instant.now()
        )
    }
}