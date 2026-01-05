package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.api.mappers.toChatParticipantDto
import com.korniykom.kotlin_chat.domain.models.ChatParticipant
import com.korniykom.kotlin_chat.infra.database.mappers.toChatParticipant
import com.korniykom.kotlin_chat.infra.database.mappers.toChatParticipantEntity
import com.korniykom.kotlin_chat.infra.database.repository.ChatParticipantRepository
import com.korniykom.kotlin_chat.type.UserId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.Locale
import java.util.Locale.getDefault

@Service
class ChatParticipantService(
    private val chatParticipantRepository: ChatParticipantRepository
) {
    fun createChatParticipant(
        chatParticipant: ChatParticipant
    ) {
        chatParticipantRepository.save(
            chatParticipant.toChatParticipantEntity()
        )
    }

    fun findChatParticipantById(userId: UserId): ChatParticipant? {
        return chatParticipantRepository.findByIdOrNull(userId)?.toChatParticipant()
    }

    fun findChatParticipantByEmailOrUsername(
        query: String
    ): ChatParticipant? {
        val normalizeQuery = query.lowercase().trim()
        return chatParticipantRepository.findByEmailOrUsername(normalizeQuery)?.toChatParticipant()
    }
}