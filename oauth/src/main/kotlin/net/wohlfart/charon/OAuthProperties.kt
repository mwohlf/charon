package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod


@ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.charon.oauth")
class OAuthProperties(

    val dataDir: String,

    val issuer: String,

    @NestedConfigurationProperty
    val allowedOrigins: Array<String>,

    @NestedConfigurationProperty
    val redirectUris: Array<String>,

    @NestedConfigurationProperty
    val clientRegistry: Map<String, ClientEntry>,
)

// @ConfigurationProperties
// class ClientRegistry: HashMap<String, ClientEntry>()

@ConstructorBinding
@ConfigurationProperties
class ClientEntry (

    @NestedConfigurationProperty
    val clientAuthenticationMethod: ClientAuthenticationMethod,

    @NestedConfigurationProperty
    val authorizationGrantType: AuthorizationGrantType,

    @NestedConfigurationProperty
    val scopes: Array<String>,

    @NestedConfigurationProperty
    val redirectUris: Array<String>,
)
