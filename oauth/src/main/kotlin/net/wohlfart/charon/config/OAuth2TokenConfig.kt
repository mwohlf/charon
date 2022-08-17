package net.wohlfart.charon.config

import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component


@Component
class OAuth2TokenConfig : OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.tokenType)) {
            context.claims.claims { claims -> claims["username"] = "just testing" }
        }
    }

}
