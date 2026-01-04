package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.UserId
import jakarta.validation.constraints.Size

data class CreateChatRequest(
    @field:Size(
        min = 1,
        message = "Chat must have at least 2 unique participants"
        )
    val otherUserIds: List<UserId>
)