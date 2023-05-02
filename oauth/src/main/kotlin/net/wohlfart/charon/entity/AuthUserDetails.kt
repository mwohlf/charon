package net.wohlfart.charon.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.*
import mu.KotlinLogging
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import kotlin.jvm.Transient


// TODO: JSON serialization

private val logger = KotlinLogging.logger {}

@Entity
@JsonDeserialize
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "user_details")
data class AuthUserDetails(

    @Column(name = "username", unique = true, nullable = false, length = 64)
    private var username: String? = null,

    @JsonIgnore
    @Column(name = "password", nullable = false, length = 64)
    private var password: String? = null,

    @Column(name = "email", unique = true, nullable = false, length = 64)
    var email: String? = null,

    @Column(name = "enabled")
    var enabled: Boolean = false,

    // @JsonSerialize(using = HibernateBagSerializer::class)
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_details_authorities",
        joinColumns = [JoinColumn(name = "user_details_id")],
        inverseJoinColumns = [JoinColumn(name = "user_authorities_id")]
    )
    var authorities: MutableList<Authority> = mutableListOf(),

    ) : UserDetails {

    @Id
    @GenericGenerator(
        name = "sequenceGenerator",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = [
            Parameter(name = "segment_value", value = "user-details-sequence")
        ]
    )
    @GeneratedValue(generator = "sequenceGenerator")
    var id: Int? = null

    @Transient // lazy init
    var grantedAuthorities: MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getUsername(): String {
        return username!!
    }

    override fun getPassword(): String {
        return password!!
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return grantedAuthorities.ifEmpty {
            grantedAuthorities = authorities
                .map { authority -> SimpleGrantedAuthority(authority.name?.name) }
                .toMutableList()
            grantedAuthorities
        }
    }
}


class HibernateBagSerializer: JsonSerializer<MutableList<Authority>>() {

    override fun serialize(value: MutableList<Authority>,
                           gen: JsonGenerator,
                           serializers: SerializerProvider) {
        // TODO implement me
        logger.info { "logger called with $value" }
    }

}
