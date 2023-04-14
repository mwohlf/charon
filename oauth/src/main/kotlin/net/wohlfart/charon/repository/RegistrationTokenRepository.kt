package net.wohlfart.charon.repository

import jakarta.persistence.*
import org.springframework.stereotype.Component


@Component
class RegistrationTokenRepository {
}


@Entity
class Registration(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?=null,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val tokenVakue: String,
)
