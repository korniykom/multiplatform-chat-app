package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.domain.events.Event
import com.korniykom.kotlin_chat.domain.events.user.UserEvent
import com.korniykom.kotlin_chat.domain.exception.InvalidTokenException
import com.korniykom.kotlin_chat.domain.exception.UserNotFoundException
import com.korniykom.kotlin_chat.domain.model.EmailVerificationToken
import com.korniykom.kotlin_chat.infra.database.entities.EmailVerificationTokenEntity
import com.korniykom.kotlin_chat.infra.database.repositories.EmailVerificationTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
import com.korniykom.kotlin_chat.infra.mappers.toEmailVerificationToken
import com.korniykom.kotlin_chat.infra.mappers.toUser
import com.korniykom.kotlin_chat.infra.message_queue.EventPublisher
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class EmailVerificationService(
    private val emailVerificationTokenRepository: EmailVerificationTokenRepository,
    private val userRepository: UserRepository,
    private val eventPublisher: EventPublisher,
    @param:Value("\${email.verification.expiry-hours}") private val expiryHours: Long
) {

    @Transactional
    fun resendVerificationEmail(email: String) {
        val token = createVerificationToken(email)

        if(token.user.hasEmailVerified) {
            return
        }

        eventPublisher.publish(
            event = UserEvent.RequestResendVerification(
                userId = token.user.id,
                verificationToken = token.token,
                userName = token.user.username,
                email = token.user.email
            )
        )

    }

    @Transactional
    fun createVerificationToken(email: String): EmailVerificationToken {
        val userEntity = userRepository.findByEmail(email)
            ?: throw UserNotFoundException()

        emailVerificationTokenRepository.invalidateActiveTokensForUser(userEntity)

        val token = EmailVerificationTokenEntity(
            expiresAt = Instant.now().plus(expiryHours, ChronoUnit.HOURS),
            user = userEntity

        )

        return emailVerificationTokenRepository.save(token).toEmailVerificationToken()
    }

    @Transactional
    fun verifyEmail(token: String) {
        val verificationToken = emailVerificationTokenRepository.findByToken(token)
            ?: throw InvalidTokenException("Email verification token is not valid")

        if (verificationToken.isUsed) {
            throw InvalidTokenException("Email verification token is already used")
        }

        if (verificationToken.isExpired) {
            throw InvalidTokenException("Email verification token is expired")
        }

        emailVerificationTokenRepository.save(
            verificationToken.apply {
                this.usedAt = Instant.now()
            }
        )

        userRepository.save(
            verificationToken.user.apply {
                this.hasVerifiedEmail = true
            }
        ).toUser()
    }

    @Scheduled(cron = "0 0 3 * * *")
    fun cleanupExpiredTokens() {
        emailVerificationTokenRepository.deleteByExpiresAtLessThan(
            Instant.now()
        )
    }
}