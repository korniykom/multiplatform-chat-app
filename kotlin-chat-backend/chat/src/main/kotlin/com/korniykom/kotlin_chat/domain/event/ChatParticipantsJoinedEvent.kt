package com.korniykom.kotlin_chat.domain.event

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.UserId

data class ChatParticipantsJoinedEvent(
    val chatId: ChatId,
    val usersId: Set<UserId>
)