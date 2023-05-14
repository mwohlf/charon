package net.wohlfart.charon.component

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken


class CharonRevocationAuthenticationProvider() : AuthenticationProvider {
    @Override
    override fun authenticate(authentication: Authentication): Authentication {
        //My implementation
        try {

            //My implementation

        } catch (ex: Exception) {
            throw OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT)
        }

        //My implementation
        return authentication
    }


    @Override
    override fun supports(authentication: Class<*>): Boolean {
        return OAuth2TokenRevocationAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

}
