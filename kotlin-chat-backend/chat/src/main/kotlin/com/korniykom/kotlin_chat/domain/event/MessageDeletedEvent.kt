package com.korniykom.kotlin_chat.domain.event

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId

data class MessageDeletedEvent(
    val messageId: ChatMessageId,
    val chatId: ChatId
)