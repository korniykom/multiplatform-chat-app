package com.korniykom.chat_backend.user.api.mappers

import com.korniykom.chat_backend.user.api.dto.AuthenticatedUserDto
import com.korniykom.chat_backend.user.api.dto.UserDto
import com.korniykom.chat_backend.user.domain.model.AuthenticatedUser
import com.korniykom.chat_backend.user.domain.model.User

fun AuthenticatedUser.toAuthenticatedUserDto(): AuthenticatedUserDto {
    return AuthenticatedUserDto(
        user = user.toUserDto(),
        accessToken = accessToken,
        refreshToken = refreshToken
    )


}

fun User.toUserDto (): UserDto {
    return UserDto(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasEmailVerified,
    )
}