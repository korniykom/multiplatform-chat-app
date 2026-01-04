package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId
import com.korniykom.kotlin_chat.type.UserId
import java.time.Instant

data class ChatMessageDto (
    val id: ChatMessageId,
    val chatId: ChatId,
    val content: String,
    val createdAt: Instant,
    val senderId: UserId
)