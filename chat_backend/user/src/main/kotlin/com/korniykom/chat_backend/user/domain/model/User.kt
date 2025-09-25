package com.korniykom.chat_backend.user.domain.model

import java.util.*

typealias UserId = UUID

data class User(
    val id: UserId,
    val username: String,
    val email: String,
    val hasEmailVerified: Boolean

)
