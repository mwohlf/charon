package net.wohlfart.charon.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.security.core.GrantedAuthority


@Entity
@Table(name = "user_authority")
data class Authority(

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false, length = 64)
    private var identifier: AuthorityIdentifier? = null

): GrantedAuthority {

    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = [
            Parameter(name = "segment_value", value = "user-authority-sequence")
        ]
    )
    @GeneratedValue(generator = "sequenceGenerator")
    var id: Int? = null

    override fun getAuthority(): String {
        return this.identifier!!.name
    }
}


enum class AuthorityIdentifier {
    ROLE_ROOT,
    ROLE_ADMIN,
    ROLE_CUSTOMER,
    ROLE_GENERAL,
    ROLE_USER,
}


