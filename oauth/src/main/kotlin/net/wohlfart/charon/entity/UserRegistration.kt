package net.wohlfart.charon.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_registration")
data class UserRegistration(

    @Id
    @SequenceGenerator(name = "user-registration-sequence-gen", sequenceName = "user-registration-sequence", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user-registration-sequence-gen")
    var id: Int? = null,

    @OneToOne(cascade = [CascadeType.PERSIST])
    var userDetails: AuthUserDetails? = null,

    @Column(name = "token", unique = true, nullable = false, length = 64)
    var tokenValue: String = UUID.randomUUID().toString(),
)
