package com.korniykom.kotlin_chat.service

import com.korniykom.kotlin_chat.domain.event.ProfilePictureUpdateEvent
import com.korniykom.kotlin_chat.domain.exception.ChatParticipantsNotFoundException
import com.korniykom.kotlin_chat.infra.database.repository.ChatParticipantRepository
import com.korniykom.kotlin_chat.infra.storage.StorageService
import com.korniykom.kotlin_chat.type.UserId
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProfilePictureService(
    private val storageService: StorageService,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun uploadProfilePicture(
        userId: UserId,
        file: MultipartFile): String {

        val participant = chatParticipantRepository.findByIdOrNull(userId)
            ?: throw ChatParticipantsNotFoundException(userId)

        val oldProfilePictureUrl = participant.profilePicture
        val newProfilePictureUrl = storageService.uploadAvatar(userId, file)

        chatParticipantRepository.save(
            participant.apply { profilePicture = newProfilePictureUrl }
        )

        if(oldProfilePictureUrl != null) {
            storageService.deleteAvatar(oldProfilePictureUrl)
        }

        applicationEventPublisher.publishEvent(
            ProfilePictureUpdateEvent(
                userId = userId,
                newUrl = newProfilePictureUrl,
            )
        )

        return newProfilePictureUrl
    }

    @Transactional
    fun deleteProfilePicture(
        userId: UserId,
    ) {
        val participant = chatParticipantRepository.findByIdOrNull(userId)
            ?: throw ChatParticipantsNotFoundException(userId)

        participant.profilePicture?.let { url ->
            chatParticipantRepository.save(
                participant.apply { profilePicture = null }
            )

            storageService.deleteAvatar(url)

            applicationEventPublisher.publishEvent(
                ProfilePictureUpdateEvent(
                    userId = userId,
                    newUrl = null,
                )
            )
        }

    }

}