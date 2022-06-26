package net.wohlfart.charon


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConstructorBinding
@ConfigurationProperties(prefix = "net.wohlfart.charon.oauth")
class OauthProperties(

    val dataDir: String,

    val issuer: String,

)
