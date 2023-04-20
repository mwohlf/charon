package net.wohlfart.charon.entity

import jakarta.persistence.*


@Entity
@Table(name = "user_authority")
data class Authority(

    var name: AuthorityName? = null

) {

    @Id
    @SequenceGenerator(name = "authority-sequence-gen", sequenceName = "authority-sequence", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "authority-sequence-gen")
    var id: Int? = null

    @ManyToMany(mappedBy = "authorities")
    var userDetails = mutableListOf<AuthUserDetails>()

}


enum class AuthorityName {
    ROLE_ROOT,
    ROLE_ADMIN,
    ROLE_CUSTOMER,
    ROLE_GENERAL,
    ROLE_USER,
}


