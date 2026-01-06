package com.korniykom.kotlin_chat.api.dto.ws

enum class IncomingWebSocketMessageType() {
    NEW_MESSAGE
}

enum class OutgoingWebSocketMessageType() {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    PROFILE_PICTURE_UPDATED,
    CHAT_PARTICIPANTS_CHANGE,
    ERROR
}

data class IncomingWebSocketMessage(
    val type: IncomingWebSocketMessageType,
    val payload: String
)


data class OutgoingWebSocketMessage(
    val type: IncomingWebSocketMessageType,
    val payload: String
)