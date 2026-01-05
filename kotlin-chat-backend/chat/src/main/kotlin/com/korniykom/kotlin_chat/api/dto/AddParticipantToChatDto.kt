package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.UserId
import jakarta.validation.constraints.Size

data class AddParticipantToChatDto(
    @field:Size(min = 1)
    val userIds: List<UserId>
)