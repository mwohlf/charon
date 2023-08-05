package net.wohlfart.charon.entity

import jakarta.persistence.*
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import java.util.*

@Entity
@Table(name = "user_registration")
data class UserRegistration(

    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = [
            Parameter(name = "segment_value", value = "user-registration-sequence")
        ]
    )
    @GeneratedValue(generator = "sequenceGenerator")
    var id: Int? = null,

    @OneToOne(cascade = [CascadeType.PERSIST])
    var userDetails: AuthUserDetails? = null,

    // token for signup/registration
    @Column(name = "token", unique = true, nullable = false, length = 64)
    var tokenValue: String = UUID.randomUUID().toString(),
)
