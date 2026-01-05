package com.korniykom.kotlin_chat.domain.exception

class ChatNotFoundException: RuntimeException(
    "Chat not found"
) {
}