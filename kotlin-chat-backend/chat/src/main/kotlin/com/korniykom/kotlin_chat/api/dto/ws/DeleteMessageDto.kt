package com.korniykom.kotlin_chat.api.dto.ws

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId

data class DeleteMessageDto(
    val chatId: ChatId,
    val messageId: ChatMessageId
)
