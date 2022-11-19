package net.wohlfart.charon.component

import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component

// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-userinfo.html
@Component
class CharonTokenCustomizer : OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.tokenType)) {
            context.claims.claims { claims -> claims["username"] = "just testing access token" }
        } else if (OidcParameterNames.ID_TOKEN == context.tokenType.value) {
            // TODO: fixme
            context.claims.claims { claims -> claims["username"] = "just testing id token" }
        }
    }

}
