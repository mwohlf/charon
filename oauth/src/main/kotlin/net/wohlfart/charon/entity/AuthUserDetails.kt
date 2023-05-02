package net.wohlfart.charon.entity

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import jakarta.persistence.*
import mu.KotlinLogging
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.IOException
import kotlin.jvm.Transient


// TODO: JSON serialization

private val logger = KotlinLogging.logger {}

@Entity
@JsonSerialize(using = UserSerializer::class)
@Table(name = "user_details")
data class AuthUserDetails(

    @Column(name = "username", unique = true, nullable = false, length = 64)
    private var username: String? = null,

    // @JsonIgnore
    @Column(name = "password", nullable = false, length = 64)
    private var password: String? = null,

    @Column(name = "email", unique = true, nullable = false, length = 64)
    var email: String? = null,

    @Column(name = "enabled")
    var enabled: Boolean = false,

    // @JsonSerialize(using = HibernateBagSerializer::class)
    // @JsonIgnore
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


class UserSerializer: JsonSerializer<AuthUserDetails>() {

    override fun serialize(value: AuthUserDetails,
                           jsonGenerator: JsonGenerator,
                           serializers: SerializerProvider) {
        logger.info { "logger called with $value" }
        jsonGenerator.writeNumberField("id", value.id!!)
        jsonGenerator.writeStringField("username", value.username)
        jsonGenerator.writeStringField("email", value.email)
        jsonGenerator.writeArrayFieldStart("roles")
        jsonGenerator.writeArray(value.grantedAuthorities.map { it.authority }.toTypedArray(), 0, value.grantedAuthorities.size)
        jsonGenerator.writeEndArray()
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serializeWithType(
        value: AuthUserDetails,
        jsonGenerator: JsonGenerator,
        provider: SerializerProvider,
        typeSerializer: TypeSerializer
    ) {
        val typeIdDef = typeSerializer.writeTypePrefix(jsonGenerator,  typeSerializer.typeId(value, JsonToken.START_OBJECT))
        serialize(value, jsonGenerator, provider) // call your customized serialize method
        typeSerializer.writeTypeSuffix(jsonGenerator, typeIdDef)
    }
}
