package com.korniykom.kotlin_chat.api.dto.ws

import com.korniykom.kotlin_chat.type.UserId

data class ProfilePictureUpdateDto(
    val userId: UserId,
    val newUrl: String?
)