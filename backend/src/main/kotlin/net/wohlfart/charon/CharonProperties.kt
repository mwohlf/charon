package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Configuration


@ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.charon")
class CharonProperties(

    val dataDir: String,

    val webjarBase: String,

    @NestedConfigurationProperty
    val api: ApiConfig,

)

@ConstructorBinding
class ApiConfig(

    val basePath: String,

)
