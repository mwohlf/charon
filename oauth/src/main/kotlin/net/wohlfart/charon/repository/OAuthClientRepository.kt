package net.wohlfart.charon.repository

import net.wohlfart.charon.OAuthProperties
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class OAuthClientRepository(
    jdbcTemplate: JdbcTemplate,
    oauthProperties: OAuthProperties,
) : JdbcRegisteredClientRepository(jdbcTemplate) {

    init {
        val publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("public-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.ADDRESS)
            .scope(OidcScopes.PROFILE) // openid profile email offline_access
            .scope(OidcScopes.EMAIL)
            .scope(OidcScopes.PHONE)
            .scope("offline_access")
            .scope("message.read")
            .scope("message.write")
            // consent to true causes another step during authorization...
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(true)
                    .requireProofKey(true)
                    .build()
            ).tokenSettings(
                TokenSettings.builder()
                    .accessTokenTimeToLive(Duration.ofSeconds(30))
                    .build()
            )

        // allowed redirects after login
        oauthProperties.redirectUris.toList().forEach {
            publicClient.redirectUri(it)
        }

        this.save(publicClient.build())
    }


}
