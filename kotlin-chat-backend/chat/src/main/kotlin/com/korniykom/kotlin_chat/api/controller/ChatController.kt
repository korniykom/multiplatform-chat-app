package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.dto.AddParticipantToChatDto
import com.korniykom.kotlin_chat.api.dto.ChatDto
import com.korniykom.kotlin_chat.api.dto.ChatMessageDto
import com.korniykom.kotlin_chat.api.dto.CreateChatRequest
import com.korniykom.kotlin_chat.api.mappers.toChatDto
import com.korniykom.kotlin_chat.api.util.requestUserId
import com.korniykom.kotlin_chat.service.ChatMessageService
import com.korniykom.kotlin_chat.service.ChatService
import com.korniykom.kotlin_chat.type.ChatId
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
    private val chatMessageService: ChatMessageService
) {
    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

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

    @PostMapping("/{chatId}/add")
    fun addChatParticipants(
        @PathVariable chatId: ChatId,
        @Valid @RequestBody body: AddParticipantToChatDto
    ): ChatDto {
        return chatService.addParticipantsToChat(userIds = body.userIds.toSet(), chatId = chatId, requestUserId = requestUserId ).toChatDto()
    }

    @DeleteMapping("/{chatId}/leave")
    fun leaveChat(
        @PathVariable chatId: ChatId,
    ) {
        chatService.removeParticipantFromTheChat(chatId = chatId, userId = requestUserId)
    }

    @GetMapping("/{chatId}/messages")
    fun getMessagesForChat(
        @PathVariable("chatId") chatId: ChatId,
        @RequestParam ("beforfe", required = false) before: Instant? = null,
        @RequestParam("pageSize", required = false) pageSize: Int = DEFAULT_PAGE_SIZE
    ): List<ChatMessageDto> {
        return chatMessageService.getChatMessages(
            chatId = chatId,
            before = before,
            pageSize = pageSize
        )
    }


}