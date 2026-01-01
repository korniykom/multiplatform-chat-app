package com.korniykom.kotlin_chat.infra.message_queue

import com.korniykom.kotlin_chat.domain.events.Event
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val rabbitTemplate: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun<T: Event> publish(event: T) {
        try {
            rabbitTemplate.convertAndSend(
                event.exchange,
                event.eventKey,
                event
            )
            logger.info("Successfully published event: ${event.eventKey}")
        } catch(e: Exception) {
            logger.error("Failed to publish ${event.eventKey}", e)
        }
    }

}