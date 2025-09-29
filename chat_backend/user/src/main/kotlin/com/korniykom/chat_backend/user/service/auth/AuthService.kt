package com.korniykom.chat_backend.user.service.auth

import com.korniykom.chat_backend.user.domain.exception.InvalidCredentialsException
import com.korniykom.chat_backend.user.domain.exception.InvalidTokenException
import com.korniykom.chat_backend.user.domain.exception.UserAlreadyExistsException
import com.korniykom.chat_backend.user.domain.exception.UserNotFoundException
import com.korniykom.chat_backend.user.domain.model.AuthenticatedUser
import com.korniykom.chat_backend.user.domain.model.User
import com.korniykom.chat_backend.user.domain.model.UserId
import com.korniykom.chat_backend.user.infra.database.entity.RefreshTokenEntity
import com.korniykom.chat_backend.user.infra.database.entity.UserEntity
import com.korniykom.chat_backend.user.infra.database.mappers.toUser
import com.korniykom.chat_backend.user.infra.database.repositories.RefreshTokenRepository
import com.korniykom.chat_backend.user.infra.database.repositories.UserRepository
import com.korniykom.chat_backend.user.infra.security.PasswordEncoder
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
    fun register(email: String, username: String, password: String): User {
        val user = userRepository.findByEmailOrUsername(
            email = email.trim(),
            username = username.trim()

        )
        if(user != null) {
            throw UserAlreadyExistsException()
        }

        val savedUser = userRepository.save(
            UserEntity(
                email = email.trim(),
                username = username.trim(),
                hashedPassword = passwordEncoder.encode(password)
            )
        ).toUser()
        return savedUser
    }

    fun login(
        email: String,
        password: String,
    ): AuthenticatedUser {
        val user = userRepository.findByEmail(email.trim())
            ?: throw InvalidCredentialsException()

        if(!passwordEncoder.matches(password, user.hashedPassword)) {
            throw InvalidCredentialsException()
        }

        //TODO: check for verified email

        return user.id?.let { userId ->
            val accessToken = jwtService.generateAccessToken(userId)
            val refreshToken = jwtService.generateRefreshToken(userId)

            storeRefreshToken(userId, refreshToken)

            AuthenticatedUser(
                user = user.toUser(),
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        } ?: throw UserNotFoundException()

    }

    private fun storeRefreshToken(userId: UserId, token:String) {
        val hashedToken = jwtService.generateRefreshToken(userId)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedToken
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}