package com.korniykom.chat_backend.user.domain.exception

class UserNotFoundException: RuntimeException(
    "User not found"
) {
}