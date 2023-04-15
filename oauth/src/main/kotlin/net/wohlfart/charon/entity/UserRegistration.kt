package net.wohlfart.charon.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_registration")
data class UserRegistration(

    @Id
    @GeneratedValue
    var id: Int? = null,

    @ManyToOne
    val userDetails: AuthUserDetails? = null,

    @Column(name = "token", nullable = false, length = 64)
    val tokenValue: String? = null,
)
