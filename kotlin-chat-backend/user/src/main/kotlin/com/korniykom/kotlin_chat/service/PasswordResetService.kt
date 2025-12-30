package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.domain.exception.InvalidCredentialException
import com.korniykom.kotlin_chat.domain.exception.InvalidTokenException
import com.korniykom.kotlin_chat.domain.exception.SamePasswordException
import com.korniykom.kotlin_chat.domain.exception.UserNotFoundException
import com.korniykom.kotlin_chat.domain.model.UserId
import com.korniykom.kotlin_chat.infra.database.entities.PasswordResetTokenEntity
import com.korniykom.kotlin_chat.infra.database.repositories.PasswordResetTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.RefreshTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
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

        // TODO Inform notification service about password reset trigger to send email
    }

    @Transactional
    fun resetPassword(token: String, newPassword: String) {
        val resetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Invalid password reset token")

        if(resetToken.isUsed) {
            throw InvalidTokenException("Reset token is already used")
        }

        if(resetToken.isExpired) {
            throw InvalidTokenException("Reset token is expired")
        }

        val user = resetToken.user

        if(passwordEncoder.matches(newPassword, user.hashedPassword)) {
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

        if(!passwordEncoder.matches(oldPassword, user.hashedPassword)) {
            throw InvalidCredentialException()
        }

        if(oldPassword == newPassword) {
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