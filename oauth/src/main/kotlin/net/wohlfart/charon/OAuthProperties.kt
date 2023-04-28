package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import java.time.Duration


@ConfigurationProperties(prefix = "net.wohlfart.charon.oauth")
class OAuthProperties(

    val dataDir: String,

    val appHomeUrl: String,

    val appLoginUrl: String,

    val issuer: String,

    @NestedConfigurationProperty
    val allowedOrigins: Array<String>,

    val clients: Map<String, ClientEntry> = HashMap(),
)

class ClientEntry (

    @NestedConfigurationProperty
    val clientAuthenticationMethod: ClientAuthenticationMethodValue,

    @NestedConfigurationProperty
    val authorizationGrantType: AuthorizationGrantTypeValue,

    @NestedConfigurationProperty
    val clientCredentials: String?, // might be null for public clients

    @NestedConfigurationProperty
    val scopes: Array<String>?,

    @NestedConfigurationProperty
    val redirectUris: Array<String>?,

    @NestedConfigurationProperty
    val postLogoutRedirectUri: String?,

    @NestedConfigurationProperty
    val accessTokenTtl: Duration?,
)

// supported for the app config
@Suppress("unused") // used in the application.yaml file
enum class ClientAuthenticationMethodValue(val value: ClientAuthenticationMethod) {
    NONE(ClientAuthenticationMethod.NONE),
    BASIC(ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
}

// supported for the app config
@Suppress("unused") // used in the application.yaml file
enum class AuthorizationGrantTypeValue(val value: AuthorizationGrantType) {
    AUTHORIZATION_CODE(AuthorizationGrantType.AUTHORIZATION_CODE),
    CLIENT_CREDENTIALS(AuthorizationGrantType.CLIENT_CREDENTIALS),
}
