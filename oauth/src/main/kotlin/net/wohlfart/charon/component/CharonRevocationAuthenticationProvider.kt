package net.wohlfart.charon.component

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken


class CharonRevocationAuthenticationProvider : AuthenticationProvider {

    @Override
    override fun authenticate(authentication: Authentication): Authentication {
        return authentication
    }

    @Override
    override fun supports(authentication: Class<*>): Boolean {
        return OAuth2TokenRevocationAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

}
