package net.wohlfart.charon.repository

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
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
            .redirectUri("http://127.0.0.1:4200")
            .redirectUri("http://127.0.0.1:4200/silent-renew.html")
            .redirectUri("http://127.0.0.1:4200/home")
            .redirectUri("http://127.0.0.1:8080")
            .redirectUri("http://127.0.0.1:8080/silent-renew.html")
            .redirectUri("http://127.0.0.1:8080/home")
            .redirectUri("https://backend.wired-heart.com/home")
            .redirectUri("https://backend.wired-heart.com/charon/home")
            .scope(OidcScopes.OPENID)
            .scope(OidcScopes.PROFILE) // openid profile email offline_access
            .scope(OidcScopes.EMAIL)
            .scope(OidcScopes.PHONE)
            .scope("offline_access")
            .scope("message.read")
            .scope("message.write")
            // consent to true causes another step during authorization...
            .clientSettings(
                ClientSettings.builder()
                    .requireAuthorizationConsent(false)
                    .requireProofKey(false)
                    .build())
            .build()
        this.save(publicClient)
    }


}
