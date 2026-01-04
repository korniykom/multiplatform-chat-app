package com.korniykom.kotlin_chat.domain.exception

class InvalidChatSizeException: RuntimeException(
    "There must be at least 2 unique participants to create chat"
) {
}