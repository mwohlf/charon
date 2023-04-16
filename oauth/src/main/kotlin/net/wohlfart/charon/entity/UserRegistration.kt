package net.wohlfart.charon.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_registration")
data class UserRegistration(

    @Id
    @GeneratedValue
    var id: Int? = null,

    @ManyToOne(cascade = [CascadeType.PERSIST])
    var userDetails: AuthUserDetails? = null,

    @Column(name = "token", unique = true, nullable = false, length = 64)
    var tokenValue: String = UUID.randomUUID().toString(),
)
