package com.korniykom.kotlin_chat.api.controller

import com.korniykom.kotlin_chat.api.dto.ChatParticipantDto
import com.korniykom.kotlin_chat.api.mappers.toChatParticipantDto
import com.korniykom.kotlin_chat.api.util.requestUserId
import com.korniykom.kotlin_chat.service.ChatParticipantService
import com.korniykom.kotlin_chat.service.ProfilePictureService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
@RestController
@RequestMapping("api/participants")
class ChatParticipantController(
    private val chatParticipantsService: ChatParticipantService,
    private val profilePictureService: ProfilePictureService
) {

    @PostMapping("/profile-picture")
    fun uploadProfilePicture(
        @RequestParam("file") file: MultipartFile
    ): String {
        return profilePictureService.uploadProfilePicture(requestUserId, file)
    }

    @DeleteMapping("/profile-picture")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteProfilePicture() {
        profilePictureService.deleteProfilePicture(requestUserId)
    }

    @GetMapping
    fun getChatParticipantByUsernameOrEmail(
        @RequestParam(required = false) query: String?
    ): ChatParticipantDto {
        val participant = if (query == null) {
            chatParticipantsService.findChatParticipantById(requestUserId)
        } else {
            chatParticipantsService.findChatParticipantByEmailOrUsername(query.trim())
        }

        return participant?.toChatParticipantDto()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }
}