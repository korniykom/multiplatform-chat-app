package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.type.UserId
import java.time.Instant

data class DeviceTokenDto(
    val userId: UserId,
    val token: String,
    val createdAt: Instant
)