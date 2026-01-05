package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.domain.exception.ChatNotFoundException
import com.korniykom.kotlin_chat.domain.exception.ChatParticipantsNotFoundException
import com.korniykom.kotlin_chat.domain.exception.ForbiddenException
import com.korniykom.kotlin_chat.domain.exception.InvalidChatSizeException
import com.korniykom.kotlin_chat.domain.models.Chat
import com.korniykom.kotlin_chat.domain.models.ChatMessage
import com.korniykom.kotlin_chat.infra.database.entities.ChatEntity
import com.korniykom.kotlin_chat.infra.database.mappers.toChat
import com.korniykom.kotlin_chat.infra.database.mappers.toChatMessage
import com.korniykom.kotlin_chat.infra.database.repository.ChatMessageRepository
import com.korniykom.kotlin_chat.infra.database.repository.ChatParticipantRepository
import com.korniykom.kotlin_chat.infra.database.repository.ChatRepository
import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.User
import com.korniykom.kotlin_chat.type.UserId
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChatService(

    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
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

    @Transactional
    fun addParticipantsToChat(
        requestUserId: UserId,
        chatId: ChatId,
        userIds: Set<UserId>
    ): Chat {
        val chat = chatRepository.findByIdOrNull(
            chatId
        ) ?: throw ChatNotFoundException()

        val isRequestingUserInChat = chat.participants.any {
            it.userId == requestUserId
        }

        if(!isRequestingUserInChat) {
            throw ForbiddenException()
        }

        val users = userIds.map { userId ->
            chatParticipantRepository.findByIdOrNull(userId)
                ?: throw ChatParticipantsNotFoundException(userId)
        }
        val lastMessage = lastMessageForChat(chatId)
        val updatedChat = chatRepository.save(
            chat.apply {
                this.participants = chat.participants + users
            }
        ).toChat(lastMessage)

        return updatedChat
    }

    private fun lastMessageForChat(chatId: ChatId): ChatMessage? {
        return chatMessageRepository.findLatestMessagesByChatIds(setOf(chatId))
            .firstOrNull()?.toChatMessage()
    }

    @Transactional
    fun removeParticipantFromTheChat(
        chatId: ChatId,
        userId: UserId
    ) {
        val chat = chatRepository.findByIdOrNull(chatId)
            ?: throw ChatNotFoundException()
        val participant = chat.participants.find{ it.userId == userId}
            ?: throw ChatParticipantsNotFoundException(userId)
        val newParticipantsSize = chat.participants.size - 1
        if(newParticipantsSize < 1) {
            chatRepository.deleteById(chatId)
            return
        }

        chatRepository.save(
            chat.apply {
                this.participants = chat.participants - participant
            }
        )
    }

}