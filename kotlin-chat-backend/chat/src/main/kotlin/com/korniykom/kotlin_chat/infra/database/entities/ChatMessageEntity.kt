package com.korniykom.kotlin_chat.infra.database.entities

import com.korniykom.kotlin_chat.type.ChatId
import com.korniykom.kotlin_chat.type.ChatMessageId
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    name = "chat_messages",
    schema = "chat_service",
    indexes = [
        Index(
            name = "idx_chat_message_chat_id_created_at",
            columnList = "chat_id,created_at DESC"
        )
    ]
)
class ChatMessageEntity(
    @Id
    @GeneratedValue(GenerationType.UUID)
    var id: ChatMessageId? = null,

    @JoinColumn(
        name = "chat_id",
        nullable = false,
        updatable = false
    )
    var chatId: ChatId,
    @Column(nullable = false)
    var content: String,
    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "chat_id",
        nullable = false,
        insertable = false,
        updatable = false
    )
    var chat: ChatEntity? = null,
    @ManyToOne
    @JoinColumn(
        name = "sender_id",
        nullable = false,
        insertable = false,
        updatable = false
    )
    var sender: ChatParticipantEntity? = null,
    @CreationTimestamp
    var createdAt: Instant = Instant.now()
)