package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.dto.ChatParticipantDto
import com.korniykom.kotlin_chat.api.mappers.toChatParticipantDto
import com.korniykom.kotlin_chat.api.util.requestUserId
import com.korniykom.kotlin_chat.service.ChatParticipantService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException

@Controller
@RequestMapping("api/chat/participants")
class ChatParticipantController(
    private val chatParticipantsService: ChatParticipantService
) {
    @GetMapping
    fun getChatParticipantByUsernameOrEmail(
        @RequestParam(required = false) query: String?
    ): ChatParticipantDto {
        val participant = if(query == null) {
            chatParticipantsService.findChatParticipantById(requestUserId)
        } else {
            chatParticipantsService.findChatParticipantByEmailOrUsername(query.trim())
        }

        return participant?.toChatParticipantDto() ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}
