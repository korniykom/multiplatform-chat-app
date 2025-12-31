package com.korniykom.kotlin_chat.infra.database.repositories

import com.korniykom.kotlin_chat.domain.model.UserId
import com.korniykom.kotlin_chat.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, UserId> {

    fun findByEmail(email: String): UserEntity?
    fun findByEmailOrUsername(email: String, username: String): UserEntity?
}