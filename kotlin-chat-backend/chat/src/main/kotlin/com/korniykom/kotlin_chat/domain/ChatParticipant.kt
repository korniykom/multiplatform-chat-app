package com.korniykom.kotlin_chat.domain

import com.korniykom.kotlin_chat.type.UserId

data class ChatParticipant(
    val userId: UserId,
    val username: String,
    val email: String,
    val profilePicture: String?
)