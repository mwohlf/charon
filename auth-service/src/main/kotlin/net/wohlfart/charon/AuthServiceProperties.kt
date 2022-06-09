package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.auth-service")
class AuthServiceProperties(

    val dataDir: String,
)
