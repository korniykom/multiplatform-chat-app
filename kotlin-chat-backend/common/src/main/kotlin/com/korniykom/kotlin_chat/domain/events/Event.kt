package com.korniykom.kotlin_chat.domain.events

import java.time.Instant

interface Event {
    val eventId: String
    val eventKey: String
    val occurredAt: Instant
    val exchange: String
}