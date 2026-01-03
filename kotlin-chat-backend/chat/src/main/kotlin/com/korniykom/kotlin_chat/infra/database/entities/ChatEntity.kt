package com.korniykom.kotlin_chat.infra.database.entities

import com.korniykom.kotlin_chat.type.ChatId
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(
    schema = "chat_service",
    name = "chats",


)
class ChatEntity(
    @Id
    @GeneratedValue(GenerationType.UUID)
    var chatId: ChatId? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "creator_id",
        nullable = false,
    )
    var creator: ChatParticipantEntity,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "chat_participants_cross_ref",
        schema = "chat_service",
        joinColumns = [JoinColumn("chat_id")],
        inverseJoinColumns = [JoinColumn("user_id")],
        indexes = [
            Index(
                name = "idx_chat_participant_chat_id_user_id",
                columnList = "chat_id, user_id",
                unique = true
            ),
            Index(
                name = "idx_chat_participant_user_id_chat_id",
                columnList = "user_id, chat_id",
                unique = true
            )
        ]
    )
    val participants: Set<ChatParticipantEntity> = emptySet(),
    @CreationTimestamp
    var createdAt: Instant = Instant.now()
)