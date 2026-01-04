package com.korniykom.kotlin_chat.domain.models

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId
import java.time.Instant

data class ChatMessage(
    val id: ChatMessageId,
    val chat: ChatId,
    val sender: ChatParticipant,
    val content: String,
    val createdAt: Instant

)