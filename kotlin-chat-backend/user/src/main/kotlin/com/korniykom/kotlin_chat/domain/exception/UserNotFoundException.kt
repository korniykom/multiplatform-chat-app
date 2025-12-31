package com.korniykom.kotlin_chat.domain.exception

class UserNotFoundException : RuntimeException(
    "User not found"
) {
}