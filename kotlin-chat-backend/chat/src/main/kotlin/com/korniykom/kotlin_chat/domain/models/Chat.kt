package com.korniykom.kotlin_chat.domain.models

import com.korniykom.kotlin_chat.type.ChatId
import java.time.Instant

data class Chat (
    val id: ChatId,
    val participants: Set<ChatParticipant>,
    val lastMessage: ChatMessage?,
    val creator: ChatParticipant,
    val lastActivityAt: Instant,
    val createdAt: Instant
)