package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import java.time.Duration


@ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.charon.oauth")
class OAuthProperties(

    val dataDir: String,

    val issuer: String,

    @NestedConfigurationProperty
    val allowedOrigins: Array<String>,

    @NestedConfigurationProperty
    val clientRegistry: Array<ClientEntry>,
)

// class ClientRegistry: HashMap<String, ClientEntry>()

@ConstructorBinding
class ClientEntry (

    val clientId: String,

    @NestedConfigurationProperty
    val clientAuthenticationMethod: ClientAuthenticationMethodValue,

    @NestedConfigurationProperty
    val authorizationGrantType: AuthorizationGrantTypeValue,

    @NestedConfigurationProperty
    val scopes: Array<String>,

    @NestedConfigurationProperty
    val redirectUris: Array<String>,

    @NestedConfigurationProperty
    val accessTokenTtl: Duration,
)

// supported for the app config
enum class ClientAuthenticationMethodValue(val value: ClientAuthenticationMethod) {

    NONE(ClientAuthenticationMethod.NONE)

}

// supported for the app config
enum class AuthorizationGrantTypeValue(val value: AuthorizationGrantType) {

    AUTHORIZATION_CODE(AuthorizationGrantType.AUTHORIZATION_CODE)

}
