package com.korniykom.kotlin_chat.infra.message_queue

import com.korniykom.kotlin_chat.domain.events.user.UserEvent
import com.korniykom.kotlin_chat.service.EmailService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Duration

@Component
@Transactional
class NotificationUserEventListener(
    private val emailService: EmailService
) {
    @RabbitListener(queues = [MessageQueues.NOTIFICATION_USER_EVENTS])
    fun handleUserEvent(event: UserEvent) {
        when(event) {
            is UserEvent.Created -> {
                emailService.sendVerificationEmail(
                    email = event.email,
                    username = event.userName,
                    userId = event.userId,
                    token = event.verificationToken
                )
            }
            is UserEvent.RequestResendVerification -> {
                emailService.sendVerificationEmail(
                    email = event.email,
                    username = event.userName,
                    userId = event.userId,
                    token = event.verificationToken
                )            }
            is UserEvent.RequestResetPassword -> {
                emailService.sendPasswordResetEmail(
                    email = event.email,
                    username = event.userName,
                    userId = event.userId,
                    token = event.passwordResetToken,
                    expiresIn = Duration.ofMinutes(event.expiresInMinutes))            }
            else -> Unit
            }
    }
}
