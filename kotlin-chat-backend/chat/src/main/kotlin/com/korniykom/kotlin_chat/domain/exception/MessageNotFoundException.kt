package com.korniykom.kotlin_chat.domain.exception

import com.korniykom.kotlin_chat.type.ChatMessageId

class MessageNotFoundException(messageId: ChatMessageId): RuntimeException(
    "Message with ID ${messageId} not found"
) {
}