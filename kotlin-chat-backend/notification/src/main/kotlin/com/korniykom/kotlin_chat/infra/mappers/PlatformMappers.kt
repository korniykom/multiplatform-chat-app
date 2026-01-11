package com.korniykom.kotlin_chat.infra.mappers

import com.korniykom.kotlin_chat.domain.model.DeviceToken
import com.korniykom.kotlin_chat.infra.database.PlatformEntity

fun DeviceToken.Platform.toPlatformEntity(): PlatformEntity {
    return when(this) {
        DeviceToken.Platform.ANDROID -> PlatformEntity.ANDROID
        DeviceToken.Platform.IOS -> PlatformEntity.IOS
    }
}

fun PlatformEntity.toPlatform(): DeviceToken.Platform {
    return when(this) {
        PlatformEntity.ANDROID -> DeviceToken.Platform.ANDROID
        PlatformEntity.IOS -> DeviceToken.Platform.IOS
    }
}