package com.korniykom.kotlin_chat.infra.database.entities

import com.korniykom.kotlin_chat.infra.security.TokenGenerator
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import kotlin.math.exp

@Entity
@Table(
    name = "email_verification_tokens",
    schema = "user_service"
)
class EmailVerificationTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(nullable = false, unique = true)
    var token: String = TokenGenerator.generateSecureToken(),
    @Column(nullable = false)
    var expiresAt: Instant,
    @Column(nullable = true)
    var usedAt: Instant? = null,
    @CreationTimestamp
    var createdAt: Instant = Instant.now(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity
) {
    val isUsed: Boolean
        get() = usedAt != null

    val isExpired: Boolean
        get() = Instant.now() > expiresAt
}