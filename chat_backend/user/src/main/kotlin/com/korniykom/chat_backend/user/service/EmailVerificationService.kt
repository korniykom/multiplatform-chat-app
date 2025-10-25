package com.korniykom.chat_backend.user.service

import com.korniykom.chat_backend.user.domain.exception.InvalidTokenException
import com.korniykom.chat_backend.user.domain.exception.UserNotFoundException
import com.korniykom.chat_backend.user.domain.model.EmailVerificationToken
import com.korniykom.chat_backend.user.infra.database.entity.EmailVerificationTokenEntity
import com.korniykom.chat_backend.user.infra.database.mappers.toEmailVerificationToken
import com.korniykom.chat_backend.user.infra.database.mappers.toUser
import com.korniykom.chat_backend.user.infra.database.repositories.EmailVerificationTokenRepository
import com.korniykom.chat_backend.user.infra.database.repositories.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
@Transactional
class EmailVerificationService(
    private val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    private val userRepository: UserRepository,
    @param:Value("\${email.verification.expiry-hours}") private val expiryHours: Long
)
{
    fun createVerificationToken(email: String): EmailVerificationToken {
        var userEntity =  userRepository.findByEmail(email)
            ?: throw UserNotFoundException()

        emailVerificationTokenRepository.invalidateActiveTokensForUser(userEntity)

        val token = EmailVerificationTokenEntity(
            expiresAt = Instant.now().plus(expiryHours, ChronoUnit.HOURS),
            user = userEntity,
        )

        return emailVerificationTokenRepository.save(token).toEmailVerificationToken()
    }

    @Transactional
    fun verifyEmail(token: String) {
        val verificationToken = emailVerificationTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Email verification token is invalid.")

        if(verificationToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used")
        }
        if(verificationToken.isExpired) {
            throw InvalidTokenException("Email verification token is expired")
        }

        emailVerificationTokenRepository.save(
            verificationToken.apply {
                this.usedAt = Instant.now()
            }
        )

        val updatedUser = userRepository.save(
            verificationToken.user.apply {
                this.hasVerifiedEmail = true
            }
        ).toUser()

    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        emailVerificationTokenRepository.deleteByExpiresAtLessThan(
            now = Instant.now()
        )
    }
}