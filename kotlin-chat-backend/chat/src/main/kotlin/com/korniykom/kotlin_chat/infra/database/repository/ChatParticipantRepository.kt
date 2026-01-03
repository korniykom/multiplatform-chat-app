package com.korniykom.kotlin_chat.infra.database.repository

import com.korniykom.kotlin_chat.infra.database.entities.ChatParticipantEntity
import com.korniykom.kotlin_chat.type.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatParticipantRepository: JpaRepository<ChatParticipantEntity, UserId> {
    fun findByUserIdIn(userIds: List<UserId>): Set<ChatParticipantEntity>
    @Query("""
        SELECT p
        FROM ChatParticipantEntity p
        WHERE LOWER(p.username) = :query or LOWER(p.email) = :query
    """)
    fun findByEmailOrUsername(query: String): ChatParticipantEntity?
}