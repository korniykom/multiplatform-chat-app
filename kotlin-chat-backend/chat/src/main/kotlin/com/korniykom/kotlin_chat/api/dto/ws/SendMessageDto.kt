package com.korniykom.kotlin_chat.api.dto.ws

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId

data class SendMessageDto(
    val messageId: ChatMessageId? = null,
    val chatId: ChatId,
    val content: String


    )