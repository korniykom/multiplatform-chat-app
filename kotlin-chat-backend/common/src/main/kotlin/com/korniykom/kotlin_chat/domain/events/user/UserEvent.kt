package com.korniykom.kotlin_chat.domain.events.user

import com.korniykom.kotlin_chat.domain.events.Event
import com.korniykom.kotlin_chat.type.UserId
import java.time.Instant
import java.util.UUID

sealed class UserEvent(
    override val eventId: String = UUID.randomUUID().toString(),
    override val exchange: String = UserEventConstants.USER_EXCHANGE,
    override val occurredAt: Instant = Instant.now(),
): Event {
    data class Created(
        val email: String,
        val userId: UserId,
        val userName: String,
        val verificationToken: String,
        override val eventKey: String = UserEventConstants.USER_CREATED_KEY
    ): UserEvent(), Event

    data class Verified(
        val email: String,
        val userId: UserId,
        val userName: String,
        override val eventKey: String = UserEventConstants.USER_VERIFIED
    ): UserEvent(), Event

    data class RequestResendVerification(
        val email: String,
        val userId: UserId,
        val userName: String,
        val verificationToken: String,
        override val eventKey: String = UserEventConstants.USER_REQUEST_RESEND_VERIFICATION
    ): UserEvent(), Event

    data class RequestResetPassword(
        val email: String,
        val userId: UserId,
        val userName: String,
        val passwordResetToken: String,
        val expiresInMinutes: Long,
        override val eventKey: String = UserEventConstants.USER_REQUEST_RESET_PASSWORD
    ): UserEvent(), Event
}