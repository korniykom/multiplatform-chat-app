package com.korniykom.kotlin_chat.infra.mappers

import com.korniykom.kotlin_chat.domain.model.DeviceToken
import com.korniykom.kotlin_chat.infra.database.DeviceTokenEntity

fun DeviceTokenEntity.toDeviceToken(): DeviceToken {
    return DeviceToken(
        userId = userId,
        token = token,
        platform = platform.toPlatform(),
        createdAt = createdAt,
        id = id
    )
}