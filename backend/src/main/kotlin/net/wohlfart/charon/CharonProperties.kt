package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.charon")
class CharonProperties(

    val dataDir: String,

    val webjarBase: String,

    val api: ApiConfig,

)

@ConstructorBinding
@ConfigurationPropertiesBinding
class ApiConfig(

    val basePath: String,

)
