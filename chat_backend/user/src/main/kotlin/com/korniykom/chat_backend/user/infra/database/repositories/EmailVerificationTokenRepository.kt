package com.korniykom.chat_backend.user.infra.database.repositories

import com.korniykom.chat_backend.user.infra.database.entity.EmailVerificationTokenEntity
import com.korniykom.chat_backend.user.infra.database.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface EmailVerificationTokenRepository: JpaRepository<EmailVerificationTokenEntity, Long> {
    fun findByToken(token: String): EmailVerificationTokenEntity?
    fun deleteByExpiresAtLessThan(now: Instant)
    fun findByUserAndUsedAtIsNull(user: UserEntity): List<EmailVerificationTokenEntity>
}