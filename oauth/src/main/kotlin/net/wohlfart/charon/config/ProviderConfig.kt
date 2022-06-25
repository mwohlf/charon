package net.wohlfart.charon.config

import net.wohlfart.charon.OauthProperties
import org.springframework.boot.autoconfigure.web.ServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings

@Configuration

class ProviderConfig(
    val oauthProperties: OauthProperties,
    val serverProperties: ServerProperties,
) {

    @Bean
    fun providerSettings(): ProviderSettings {
        val serverPort = serverProperties.port
        return ProviderSettings.builder().issuer("http://localhost:${serverPort}/authority" ).build()
    }

}
