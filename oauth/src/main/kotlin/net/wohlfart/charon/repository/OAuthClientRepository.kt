package net.wohlfart.charon.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.stereotype.Component
import java.util.*

@Component
class OAuthClientRepository(
    jdbcTemplate: JdbcTemplate,
) : JdbcRegisteredClientRepository(jdbcTemplate) {

    init {
        val publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("public-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://127.0.0.1:4200")
            .redirectUri("http://localhost:4200")
            .redirectUri("http://127.0.0.1:4200/silent-renew.html")
            .scope(OidcScopes.OPENID)
            .scope("message.read")
            .scope("message.write")
            // consent to true causes another step during authorization...
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).requireProofKey(true).build())
            .build()
        this.save(publicClient)
    }


}
