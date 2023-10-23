package net.wohlfart.charon.entity

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant

// TODO: update existing instead of re-writing for each login
data class ExternalTokenId(
    var type: TokenType? = null,
    var authUserDetails: Int? = null,
) : Serializable

enum class TokenType {
    ACCESS,
    REFRESH,
    ID,
}


@Entity
@Table(
    name = "external_token",
    uniqueConstraints = [
        // user_details has at most a 1-1 relationship to an external OAuth provider,
        // and we only want at most one token of each time per provider, when a user re-logs
        // the older token needs to be re-used or removed
        UniqueConstraint(columnNames = ["user_details_id", "type"])
    ]
)
@IdClass(ExternalTokenId::class)
data class ExternalToken(

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = false, nullable = false, length = 10)
    val type: TokenType,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_id")
    val authUserDetails: AuthUserDetails,

    @Column(name = "value", nullable = false, length = 5000)
    val value: String,

    @Column(name = "issued_at", nullable = true)
    val issuedAt: Instant?,

    @Column(name = "expired_at", nullable = true)
    val expiredAt: Instant?,
)

