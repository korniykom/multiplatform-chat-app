package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.domain.events.user.UserEvent
import com.korniykom.kotlin_chat.domain.exception.*
import com.korniykom.kotlin_chat.domain.model.AuthenticatedUser
import com.korniykom.kotlin_chat.type.User
import com.korniykom.kotlin_chat.type.UserId
import com.korniykom.kotlin_chat.infra.database.entities.RefreshTokenEntity
import com.korniykom.kotlin_chat.infra.database.entities.UserEntity
import com.korniykom.kotlin_chat.infra.database.repositories.RefreshTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
import com.korniykom.kotlin_chat.infra.mappers.toUser
import com.korniykom.kotlin_chat.infra.message_queue.EventPublisher
import com.korniykom.kotlin_chat.infra.security.PasswordEncoder
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.*

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val emailVerificationService: EmailVerificationService,
    private val eventPublisher: EventPublisher
) {
    fun login(
        email: String,
        password: String
    ): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialException()

        if (!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialException()
        }
        if (!user.hasVerifiedEmail) {
            throw EmailNotVerifiedException()
        }

        return user.id?.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, refreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional
    fun refresh(refreshToken: String): AuthenticatedUser {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw InvalidTokenException(
                "Invalid refresh token"
            )
        }

        val userId = jwtService.gerUserIdFromToken(refreshToken)
        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException()

        val hashed = hashToken(refreshToken)
        return user.id?.let { userId ->
            refreshTokenRepository.findByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            ) ?: throw InvalidTokenException("Invalid refresh token")

            refreshTokenRepository.deleteByUserIdAndHashedToken(
                userId = userId,
                hashedToken = hashed
            )

            val newRefreshToken = jwtService.generateAccessToken(userId)
            val newAccessToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, newRefreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = newAccessToken,
                refreshToken = newRefreshToken
            )
        } ?: throw UserNotFoundException()
    }

    @Transactional
    fun logout(refreshToken: String) {
        val userId = jwtService.gerUserIdFromToken(refreshToken)
        val hashed = hashToken(refreshToken)
        refreshTokenRepository.deleteByUserIdAndHashedToken(
            userId = userId,
            hashedToken = hashed
        )
    }

    @Transactional
    fun register(email: String, username: String, password: String): User {
        val trimmedEmail = email.trim()
        val user = userRepository.findByEmailOrUsername(trimmedEmail, username.trim())

        if (user != null) {
            throw UserAlreadyExistsException()
        }


        val savedUser = userRepository.saveAndFlush(
            UserEntity(
                id = null,
                email = trimmedEmail,
                username = username.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()

        val token = emailVerificationService.createVerificationToken(trimmedEmail)

        eventPublisher.publish(
            event = UserEvent.Created(
                userId = savedUser.id,
                email = savedUser.email,
                userName = savedUser.username,
                verificationToken = token.token
            )
        )

        return savedUser
    }

    private fun storeRefreshToken(userId: UserId, token: String) {
        val hashed = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}