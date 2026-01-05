package com.korniykom.kotlin_chat.infra.messaging

import com.korniykom.kotlin_chat.domain.events.user.UserEvent
import com.korniykom.kotlin_chat.domain.models.ChatParticipant
import com.korniykom.kotlin_chat.infra.message_queue.MessageQueues
import com.korniykom.kotlin_chat.service.ChatParticipantService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ChatUserEventListener(
    private val chatParticipantService: ChatParticipantService,

) {
    @RabbitListener(queues = [MessageQueues.CHAT_USER_EVENTS])
    fun handleUserEvent(
        event: UserEvent
    ) {
        when(event) {
            is UserEvent.Verified -> {
                chatParticipantService.createChatParticipant(
                    chatParticipant = ChatParticipant(
                        userId = event.userId,
                        username = event.userName,
                        email = event.email,
                        profilePicture = null,
                    )
                )
            }
            else -> Unit
        }
    }
}