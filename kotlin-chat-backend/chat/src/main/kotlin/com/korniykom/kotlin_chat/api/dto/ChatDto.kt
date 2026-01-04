package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.ChatId
import java.time.Instant

data class ChatDto(
    val chatId: ChatId,
    val participants: List<ChatParticipantDto>,
    val lastActivityAt: Instant,
    val lastMessage: ChatMessageDto?,
    val creator: ChatParticipantDto
)