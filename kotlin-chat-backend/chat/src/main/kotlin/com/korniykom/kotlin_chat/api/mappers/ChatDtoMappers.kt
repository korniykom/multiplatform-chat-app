package com.korniykom.kotlin_chat.api.mappers

import com.korniykom.kotlin_chat.api.dto.ChatDto
import com.korniykom.kotlin_chat.api.dto.ChatMessageDto
import com.korniykom.kotlin_chat.api.dto.ChatParticipantDto
import com.korniykom.kotlin_chat.domain.models.Chat
import com.korniykom.kotlin_chat.domain.models.ChatMessage
import com.korniykom.kotlin_chat.domain.models.ChatParticipant

fun Chat.toChatDto() : ChatDto {
    return ChatDto(
        chatId = id,
        participants = participants.map {
            it.toChatParticipantDto()
        },
            lastActivityAt = lastActivityAt,
        lastMessage = lastMessage?.toChatMessageDto(),
        creator = creator.toChatParticipantDto(),
        )
}

fun ChatMessage.toChatMessageDto() : ChatMessageDto {
    return ChatMessageDto(
        id = id,
        chatId = chat,
        content = content,
        createdAt = createdAt,
        senderId = sender.userId

    )
}

fun ChatParticipant.toChatParticipantDto() : ChatParticipantDto {
    return ChatParticipantDto(
        userId = userId,
        username = username,
        email = email,
        profilePicture = profilePicture
    )
}