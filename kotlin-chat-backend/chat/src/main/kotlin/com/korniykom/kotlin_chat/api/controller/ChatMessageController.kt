package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.util.requestUserId
import com.korniykom.kotlin_chat.infra.database.repository.ChatMessageRepository
import com.korniykom.kotlin_chat.service.ChatMessageService
import com.korniykom.kotlin_chat.type.ChatMessageId
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/messages")
class ChatMessageController(
    private val chatMessageService: ChatMessageService,
    private val chatMessageRepository: ChatMessageRepository
) {
    @DeleteMapping("/{messageId}")
    fun deleteMessage(
        @PathVariable("messageId") messageId: ChatMessageId,
    ) {
        chatMessageService.deleteMessage(messageId = messageId, requesterId = requestUserId)
    }
}