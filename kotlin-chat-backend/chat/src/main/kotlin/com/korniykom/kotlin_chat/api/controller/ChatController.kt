package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.dto.ChatDto
import com.korniykom.kotlin_chat.api.dto.CreateChatRequest
import com.korniykom.kotlin_chat.api.mappers.toChatDto
import com.korniykom.kotlin_chat.api.requestUserId
import com.korniykom.kotlin_chat.service.ChatService
import jakarta.validation.Valid
import org.springframework.data.annotation.CreatedBy
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService
) {
    @PostMapping
    fun createChat(
        @Valid
        @RequestBody body: CreateChatRequest
    ) : ChatDto {
        return chatService.createChat(
            creatorId = requestUserId,
            otherUserIds = body.otherUserIds.toSet()
        ).toChatDto()
    }
}