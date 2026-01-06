package com.korniykom.kotlin_chat.domain.events.chat

import com.korniykom.kotlin_chat.domain.events.Event
import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.UserId
import java.time.Instant
import java.util.UUID

sealed class ChatEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val exchange: String = ChatEventConstants.CHAT_EXCHANGE,
    override val occurredAt: Instant = Instant.now(),
): Event {
    data class NewMessage(
        val senderId: UserId,
        val senderUsername: String,
        val recipientIds: Set<UserId>,
        val chatId: ChatId,
        val message: String,
        override val eventKey: String = ChatEventConstants.CHAT_NEW_MESSAGE
    ): ChatEvent(), Event {

    }
}