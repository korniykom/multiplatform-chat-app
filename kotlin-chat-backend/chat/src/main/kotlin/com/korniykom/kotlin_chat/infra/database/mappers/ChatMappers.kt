package com.korniykom.kotlin_chat.infra.database.mappers

import com.korniykom.kotlin_chat.domain.models.Chat
import com.korniykom.kotlin_chat.domain.models.ChatMessage
import com.korniykom.kotlin_chat.domain.models.ChatParticipant
import com.korniykom.kotlin_chat.infra.database.entities.ChatEntity
import com.korniykom.kotlin_chat.infra.database.entities.ChatParticipantEntity

fun ChatEntity.toChat(lastMessage: ChatMessage? = null): Chat {
    return Chat(
        id = chatId!!,
        createdAt = createdAt,
        creator = creator.toChatParticipant(),
        participants = participants.map {
            it.toChatParticipant()
        }.toSet(),
        lastActivityAt = lastMessage?.createdAt ?: createdAt,
        lastMessage = lastMessage
    )
}

fun ChatParticipantEntity.toChatParticipant(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        email = email,
        username = username,
        profilePicture = profilePicture
    )
}