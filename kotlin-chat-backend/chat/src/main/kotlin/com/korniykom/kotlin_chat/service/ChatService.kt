package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.domain.exception.ChatParticipantsNotFoundException
import com.korniykom.kotlin_chat.domain.exception.InvalidChatSizeException
import com.korniykom.kotlin_chat.domain.models.Chat
import com.korniykom.kotlin_chat.infra.database.entities.ChatEntity
import com.korniykom.kotlin_chat.infra.database.mappers.toChat
import com.korniykom.kotlin_chat.infra.database.repository.ChatParticipantRepository
import com.korniykom.kotlin_chat.infra.database.repository.ChatRepository
import com.korniykom.kotlin_chat.type.UserId
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatService(

    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
) {
    @Transactional
    fun createChat(
        creatorId: UserId,
        otherUserIds: Set<UserId>,

    ): Chat {
        val otherParticipants = chatParticipantRepository.findByUserIdIn(
            userIds = otherUserIds
        )

        val allParticipants = (otherParticipants + creatorId)

        if(allParticipants.size < 2) {
            throw InvalidChatSizeException()
        }

        val creator = chatParticipantRepository.findByIdOrNull(creatorId)
            ?: throw ChatParticipantsNotFoundException(creatorId)

        return chatRepository.save(
            ChatEntity(
                creator = creator,
                participants = setOf(creator) + otherParticipants
            )
        ).toChat()
    }
}