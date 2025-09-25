package com.korniykom.chat_backend.user.infra.database.repositories

import com.korniykom.chat_backend.user.domain.model.UserId
import com.korniykom.chat_backend.user.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<UserEntity, UserId> {
    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}