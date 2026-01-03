package com.korniykom.kotlin_chat.domain

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId
import java.time.Instant

data class ChatMessage(
    val id: ChatMessageId,
    val chat: ChatId,
    val sander: ChatParticipant,
    val content: String,
    val createdAt: Instant

)