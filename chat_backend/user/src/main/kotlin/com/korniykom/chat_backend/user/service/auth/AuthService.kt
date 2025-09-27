package com.korniykom.chat_backend.user.service.auth

import com.korniykom.chat_backend.user.domain.exception.UserAlreadyExistsException
import com.korniykom.chat_backend.user.domain.model.User
import com.korniykom.chat_backend.user.infra.database.entity.UserEntity
import com.korniykom.chat_backend.user.infra.database.mappers.toUser
import com.korniykom.chat_backend.user.infra.database.repositories.UserRepository
import com.korniykom.chat_backend.user.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
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

}