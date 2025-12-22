package com.korniykom.kotlin_chat.api.mappers

import com.korniykom.kotlin_chat.api.dto.AuthenticatedUserDto
import com.korniykom.kotlin_chat.api.dto.UserDto
import com.korniykom.kotlin_chat.domain.model.AuthenticatedUser
import com.korniykom.kotlin_chat.domain.model.User

fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto {
    return AuthenticatedUserDto(
        user = user.toUserDto(),
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun User.toUserDto(): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerified
    )
}