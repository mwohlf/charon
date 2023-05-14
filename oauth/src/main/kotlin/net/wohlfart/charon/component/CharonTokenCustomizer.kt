package net.wohlfart.charon.component

import org.springframework.security.core.Authentication
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component


// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-userinfo.html
@Component
class CharonTokenCustomizer( val sessionRegistry: SessionRegistry) : OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.tokenType)) {
            context.claims.claims { claims -> claims["userName"] = "just testing access token" }
        } else if (OidcParameterNames.ID_TOKEN == context.tokenType.value) {
            // we need the sid in the id token for openid logout to work and delete the session
            val principal: Authentication = context.getPrincipal()
            val sessions = sessionRegistry.getAllSessions(principal.principal, false)
            // TODO: fail if we have more than one
            if (sessions.size == 1) {
                // sid is needed in the openid logout endpoint
                context.claims.claims { claims -> claims["sid"] = sessions[0].sessionId }
            }
        }
    }

}
