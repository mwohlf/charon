package net.wohlfart.charon.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter


@Entity
@Table(name = "user_authority")
data class Authority(

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false, length = 64)
    var name: AuthorityName? = null

) {

    @Id
    @GenericGenerator(
        name = "userAuthoritySequenceGenerator",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = [
            Parameter(name = "segment_value", value = "user-authority-sequence")
        ]
    )
    @GeneratedValue(generator = "userAuthoritySequenceGenerator")
    var id: Int? = null

    // @ManyToMany(mappedBy = "authorities")
    // var userDetails = mutableListOf<AuthUserDetails>()

}


enum class AuthorityName {
    ROLE_ROOT,
    ROLE_ADMIN,
    ROLE_CUSTOMER,
    ROLE_GENERAL,
    ROLE_USER,
}


