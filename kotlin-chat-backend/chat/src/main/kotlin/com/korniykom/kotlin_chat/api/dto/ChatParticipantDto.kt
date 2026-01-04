package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.UserId

data class ChatParticipantDto (
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePicture: String?
)