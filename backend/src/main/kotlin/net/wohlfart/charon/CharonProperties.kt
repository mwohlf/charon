package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty


@ConfigurationProperties(prefix = "net.wohlfart.charon")
class CharonProperties(

    val dataDir: String,

    val webjarBase: String,

    val externalTokenUri: String,

    @NestedConfigurationProperty
    val api: ApiConfig,

    @NestedConfigurationProperty
    val oauthClients: Array<ClientConfig>,
)

class ApiConfig(

    val basePath: String,

    val loggingPath: String,
)

class ClientConfig(

    val configId: String,

    val issuerUri: String,

    val clientId: String,

    val postLogoutRedirectUri: String?,
)

