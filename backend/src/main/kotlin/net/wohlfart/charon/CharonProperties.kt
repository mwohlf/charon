package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.bind.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Configuration


// @ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.charon")
class CharonProperties(

    val dataDir: String,

    val webjarBase: String,

    @NestedConfigurationProperty
    val api: ApiConfig,

    @NestedConfigurationProperty
    val oauthClients: Array<ClientConfig>,

)

// @ConstructorBinding
class ApiConfig(

    val basePath: String,

    val logging: String,

    val oauth: String,

)

// @ConstructorBinding
class ClientConfig(

    val configId: String,

    val issuerUri: String,

    val clientId: String,

)


/*

{
    "issuer": "http://oauth.finalrestingheartrate.com",
    "authorization_endpoint": "http://oauth.finalrestingheartrate.com/oauth2/authorize",
    "token_endpoint": "http://oauth.finalrestingheartrate.com/oauth2/token",
    "token_endpoint_auth_methods_supported": [
        "client_secret_basic",
        "client_secret_post",
        "client_secret_jwt",
        "private_key_jwt"
    ],
    "jwks_uri": "http://oauth.finalrestingheartrate.com/oauth2/jwks",
    "userinfo_endpoint": "http://oauth.finalrestingheartrate.com/userinfo",
    "response_types_supported": [
        "code"
    ],
    "grant_types_supported": [
        "authorization_code",
        "client_credentials",
        "refresh_token"
    ],
    "revocation_endpoint": "http://oauth.finalrestingheartrate.com/oauth2/revoke",
    "revocation_endpoint_auth_methods_supported": [
        "client_secret_basic",
        "client_secret_post",
        "client_secret_jwt",
        "private_key_jwt"
    ],
    "introspection_endpoint": "http://oauth.finalrestingheartrate.com/oauth2/introspect",
    "introspection_endpoint_auth_methods_supported": [
        "client_secret_basic",
        "client_secret_post",
        "client_secret_jwt",
        "private_key_jwt"
    ],
    "subject_types_supported": [
        "public"
    ],
    "id_token_signing_alg_values_supported": [
        "RS256"
    ],
    "scopes_supported": [
        "openid"
    ]
}

 */
