package net.wohlfart.charon.config

import net.wohlfart.charon.OauthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings

@Configuration

class ProviderConfig(
    val oauthProperties: OauthProperties,
    // val serverProperties: ServerProperties,
) {

    @Bean
    fun providerSettings(): ProviderSettings {
        // val contextPath = serverProperties.servlet.contextPath
        // val serverPort = serverProperties.port
        return ProviderSettings.builder()
            // this will show up in the issuer field, but will also be prefix for
            // - authorization_endpoint
            // - token_endpoint
            // - jwks_uri
            // - userinfo_endpoint
            // for issues info see: https://openid.net/specs/openid-connect-discovery-1_0.html#ProviderConfig
            // .issuer("issuer1") // issuer must be a valid URL
            // .issuer("http://localhost:${serverPort}/issuers/issuer1/" )
            .issuer(oauthProperties.issuer)
            .build()
    }

    // OidcProviderConfigurationEndpointFilter implements the url for the
    //   http://localhost:8081/.well-known/openid-configuration endpoint
}
