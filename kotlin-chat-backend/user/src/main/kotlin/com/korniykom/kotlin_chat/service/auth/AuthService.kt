package com.korniykom.kotlin_chat.service.auth

import com.korniykom.kotlin_chat.domain.exception.UserAlreadyExistsException
import com.korniykom.kotlin_chat.domain.model.User
import com.korniykom.kotlin_chat.infra.database.entities.UserEntity
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
import com.korniykom.kotlin_chat.infra.mappers.toUser
import com.korniykom.kotlin_chat.infra.security.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
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
}