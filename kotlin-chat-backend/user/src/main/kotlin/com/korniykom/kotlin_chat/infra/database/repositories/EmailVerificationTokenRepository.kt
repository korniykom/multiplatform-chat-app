package com.korniykom.kotlin_chat.infra.database.repositories

import com.korniykom.kotlin_chat.infra.database.entities.EmailVerificationTokenEntity
import com.korniykom.kotlin_chat.infra.database.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface EmailVerificationTokenRepository: JpaRepository<EmailVerificationTokenEntity, Long> {
    fun findByToken(token: String): EmailVerificationTokenEntity?
    fun deleteByExpiresAtLessThan(date: Instant)
    fun findByUserAndUsedAtIsNull(user: UserEntity): List<EmailVerificationTokenEntity>
}