package net.wohlfart.charon.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.security.core.GrantedAuthority


@Entity
@Table(name = "external_token")
data class ExternalToken(

    @Enumerated(EnumType.STRING)
    @Column(name = "type", unique = false, nullable = false, length = 10)
    var type: TokenType? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", unique = false, nullable = false, length = 10)
    var provider: TokenProvider? = null,

    @Column(name = "value", unique = true, nullable = false, length = 5000)
    var value: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_details_id")
    var authUserDetails: AuthUserDetails,

) {

    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = [
            Parameter(name = "segment_value", value = "external-token-sequence")
        ]
    )
    @GeneratedValue(generator = "sequenceGenerator")
    var id: Int? = null


}


enum class TokenType {
    ACCESS,
    REFRESH,
    ID,
}

enum class TokenProvider {
    GOOGLE,
    FACEBOOK,
    AMAZON,
}


