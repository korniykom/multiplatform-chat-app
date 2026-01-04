package com.korniykom.kotlin_chat.domain.exception

import com.korniykom.kotlin_chat.type.UserId

class ChatParticipantsNotFoundException(private val id: UserId): RuntimeException(
    "The chat participant with ID $id not found"
) {
}