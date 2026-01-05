package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.api.dto.ChatMessageDto
import com.korniykom.kotlin_chat.api.mappers.toChatMessageDto
import com.korniykom.kotlin_chat.domain.exception.ChatNotFoundException
import com.korniykom.kotlin_chat.domain.exception.ChatParticipantsNotFoundException
import com.korniykom.kotlin_chat.domain.exception.ForbiddenException
import com.korniykom.kotlin_chat.domain.exception.MessageNotFoundException
import com.korniykom.kotlin_chat.domain.models.ChatMessage
import com.korniykom.kotlin_chat.infra.database.entities.ChatMessageEntity
import com.korniykom.kotlin_chat.infra.database.mappers.toChatMessage
import com.korniykom.kotlin_chat.infra.database.repository.ChatMessageRepository
import com.korniykom.kotlin_chat.infra.database.repository.ChatParticipantRepository
import com.korniykom.kotlin_chat.infra.database.repository.ChatRepository
import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId
import com.korniykom.kotlin_chat.type.UserId
import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChatMessageService(
    private val chatRepository: ChatRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatParticipantRepository: ChatParticipantRepository
) {

    fun getChatMessages(
        chatId: ChatId,
        before: Instant?,
        pageSize: Int

    ): List<ChatMessageDto> {
        return chatMessageRepository.findByChatIdBefore(
            chatId = chatId,
            before = before ?: Instant.now(),
            pageable = PageRequest.of(0, pageSize)
        )
            .content
            .asReversed()
            .map {
                it.toChatMessage().toChatMessageDto()
            }
    }

    @Transactional
    fun sendMessage(
        chatId: ChatId,
        senderId: UserId,
        content: String,
        messageId: ChatMessageId?
    ): ChatMessage {
        val chat = chatRepository.findChatById(chatId, senderId)
            ?: throw ChatNotFoundException()

        val sender = chatParticipantRepository.findByIdOrNull(senderId)
            ?: throw ChatParticipantsNotFoundException(senderId)

        val savedMessage = chatMessageRepository.save(
            ChatMessageEntity(
                id = messageId,
                content = content.trim(),
                chatId = chatId,
                sender = sender
            )
        )

        return savedMessage.toChatMessage()
    }

    @Transactional
    fun deleteMessage(
        messageId: ChatMessageId,
        requesterId: UserId
        ) {
        val message = chatMessageRepository.findByIdOrNull(messageId)
            ?: throw MessageNotFoundException(messageId)

        if(message.sender.userId != requesterId) {
            throw ForbiddenException()
        }

        chatMessageRepository.deleteById(messageId)

    }
}