package net.wohlfart.charon.repository

import mu.KotlinLogging
import net.wohlfart.charon.ClientEntry
import net.wohlfart.charon.OAuthProperties
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component
import java.util.*

private val logger = KotlinLogging.logger {}


// TODO: read all from config
fun buildClient(clientId: String, clientEntry: ClientEntry): RegisteredClient {

    logger.info { "registering $clientId" }

    val publicClient = RegisteredClient.withId(UUID.randomUUID().toString())

    publicClient.clientId(clientId)
        .clientAuthenticationMethod(clientEntry.clientAuthenticationMethod.value)
        .authorizationGrantType(clientEntry.authorizationGrantType.value)
    // .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
    // consent to true causes another step during authorization...

    publicClient.clientSettings(
        ClientSettings.builder()
            .requireAuthorizationConsent(false)
            .requireProofKey(true)
            .build()
    )

    publicClient.tokenSettings(
        TokenSettings.builder()
            .accessTokenTimeToLive(clientEntry.accessTokenTtl)
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
        oauthProperties.clients.forEach {
            this.save(buildClient(it.key, it.value))
        }
    }

}
