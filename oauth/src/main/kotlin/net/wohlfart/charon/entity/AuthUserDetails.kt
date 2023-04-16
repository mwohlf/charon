package net.wohlfart.charon.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import kotlin.jvm.Transient


// TODO: JSON serialization

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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    var authorities: MutableList<Authority> = mutableListOf(),

) : UserDetails {

    @Id
    @GeneratedValue
    var id: Int? = null

    @Transient
    var grantedAuthorities: MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun getUsername(): String? {
        return username
    }

    override fun getPassword(): String? {
        return password
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
