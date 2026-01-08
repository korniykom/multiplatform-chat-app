package com.korniykom.kotlin_chat.domain.event

import com.korniykom.kotlin_chat.type.UserId

data class ProfilePictureUpdateEvent(
    val userId: UserId,
    val newUrl: String?
)