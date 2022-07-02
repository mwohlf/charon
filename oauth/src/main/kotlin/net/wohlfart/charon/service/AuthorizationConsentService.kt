
package net.wohlfart.charon.service

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service

@Service
class AuthorizationConsentService(
    jdbcTemplate: JdbcTemplate,
    registeredClientRepository: RegisteredClientRepository,
) : JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository) {

    init {


    }

}

