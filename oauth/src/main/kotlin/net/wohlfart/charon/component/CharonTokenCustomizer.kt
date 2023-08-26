package net.wohlfart.charon.component

import mu.KotlinLogging
import net.wohlfart.charon.entity.AuthUserDetails
import net.wohlfart.charon.service.AuthUserDetailsService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-userinfo.html
@Component
class CharonTokenCustomizer(
    // val sessionRegistry: SessionRegistry,
    val authUserDetailsService: AuthUserDetailsService,
) : OAuth2TokenCustomizer<JwtEncodingContext> {

    override fun customize(context: JwtEncodingContext) {

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.tokenType)) {
            context.claims.claims { claims -> claims["userName"] = "just testing access token" }


        } else if (OidcParameterNames.ID_TOKEN == context.tokenType.value) {
            context.claims.claims { claims -> claims["testclaim"] = "this is a id token" }

            val authentication: Authentication = context.getPrincipal()
            logger.info { "authentication: $authentication"  }

            when (authentication){
                // local login with username/passwd
                is UsernamePasswordAuthenticationToken -> {
                    logger.info { "UsernamePasswordAuthenticationToken: $authentication" }
                    val authUserDetails = authentication.principal as AuthUserDetails
                    context.claims.claims { claims -> claims["xid"] = authUserDetails.xid }
                }
                // from a federated auth provider
                is OAuth2AuthenticationToken -> {
                    logger.info { "OAuth2AuthenticationToken: $authentication"  }
                    val federatedClient = authentication.authorizedClientRegistrationId
                    val user = authentication.principal as DefaultOidcUser
                    val federatedId = user.attributes["sub"] as String
                    val email = user.attributes["email"] as String
                    val authUserDetails = authUserDetailsService.createFederatedUser(email, federatedClient, federatedId)
                    context.claims.claims { claims -> claims["xid"] = authUserDetails.xid }
                }
                // TODO: throw an exception
                else -> {
                    logger.info { "else: $authentication"  }
                }
            }


            // we need the sid in the id token for openid logout to work and delete the session



            // val userDetails = context.getPrincipal<OAuth2AuthenticationToken?>().details

            // val clientRegistrationId = principal.authorizedClientRegistrationId
            // principal.attributes

            // logger.info { "userDetails: $userDetails"  }
            // val sessions = sessionRegistry.getAllSessions(principal.principal, false)
            // TODO: fail if we have more than one
            // if (sessions.size == 1) {
            //    // sid is needed in the openid logout endpoint
            //    context.claims.claims { claims -> claims["sid"] = sessions[0].sessionId }
            // }
        }
    }

}
