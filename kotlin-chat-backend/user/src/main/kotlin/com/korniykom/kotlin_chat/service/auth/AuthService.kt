package com.korniykom.kotlin_chat.service.auth

import com.korniykom.kotlin_chat.domain.exception.InvalidCredentialException
import com.korniykom.kotlin_chat.domain.exception.UserAlreadyExistsException
import com.korniykom.kotlin_chat.domain.exception.UserNotFoundException
import com.korniykom.kotlin_chat.domain.model.AuthenticatedUser
import com.korniykom.kotlin_chat.domain.model.User
import com.korniykom.kotlin_chat.domain.model.UserId
import com.korniykom.kotlin_chat.infra.database.entities.RefreshTokenEntity
import com.korniykom.kotlin_chat.infra.database.entities.UserEntity
import com.korniykom.kotlin_chat.infra.database.repositories.RefreshTokenRepository
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
import com.korniykom.kotlin_chat.infra.mappers.toUser
import com.korniykom.kotlin_chat.infra.security.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    fun login(
        email: String,
        password: String
    ): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
           ?: throw InvalidCredentialException()

        if(!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialException()
        }
        // TODO: check for verified email

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

    fun register(email: String, username: String, password: String): User {
        val user = userRepository.findByEmailOrUsername(email.trim(), username.trim())

        if(user != null) {
            throw UserAlreadyExistsException()
        }

        val savedUser = userRepository.save(
            UserEntity(
                id = null,
                email = email.trim(),
                username = username.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()
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