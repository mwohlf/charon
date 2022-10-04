package net.wohlfart.charon.repository

import net.wohlfart.charon.ClientEntry
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


// TODO: read all from config
fun buildClient(clientEntry: ClientEntry): RegisteredClient {
    val publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
        .clientId(clientEntry.clientId)
        .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
        // consent to true causes another step during authorization...
        .clientSettings(
            ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .requireProofKey(true)
                .build()
        ).tokenSettings(
            TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(30))
                .build()
        )

    // allowed redirects after login
    clientEntry.redirectUris.toList().forEach {
        publicClient.redirectUri(it)
    }

    // scopes
    clientEntry.scopes.toList().forEach {
        publicClient.scope(it)
    }

    return publicClient.build()
}

@Component
class OAuthClientRepository(
    jdbcTemplate: JdbcTemplate,
    oauthProperties: OAuthProperties,
) : JdbcRegisteredClientRepository(jdbcTemplate) {

    init {
        oauthProperties.clientRegistry.forEach {
            this.save(buildClient(it))
        }
    }

}
