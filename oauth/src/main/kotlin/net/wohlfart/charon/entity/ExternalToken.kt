package net.wohlfart.charon.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.io.Serializable

// TODO: update existing instead of re-writing for each login
data class ExternalTokenId(
    var id: Int? = null,
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
        // TODO
        // UniqueConstraint(columnNames = ["user_details_id", "type"])
    ]
)
@IdClass(ExternalTokenId::class)
data class ExternalToken(

    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = [
            Parameter(name = "segment_value", value = "external-token-sequence")
        ]
    )
    @GeneratedValue(generator = "sequenceGenerator")
    var id: Int? = null,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_id")
    var authUserDetails: AuthUserDetails,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = false, nullable = false, length = 10)
    var type: TokenType? = null,

    @Column(name = "value", unique = true, nullable = false, length = 5000)
    var value: String? = null,

    )

