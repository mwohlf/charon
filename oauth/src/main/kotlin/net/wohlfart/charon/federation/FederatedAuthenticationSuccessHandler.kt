package net.wohlfart.charon.federation

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import net.wohlfart.charon.service.AuthUserDetailsService
import net.wohlfart.charon.service.TokenService
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class FederatedAuthenticationSuccessHandler(
    val clientService: OAuth2AuthorizedClientService,
    val tokenService: TokenService,
    val authUserDetailsService: AuthUserDetailsService,
) : AuthenticationSuccessHandler {

    private val delegate: AuthenticationSuccessHandler = SavedRequestAwareAuthenticationSuccessHandler()

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        logger.info { "<onAuthenticationSuccess> for request: $request" }
        if (authentication is OAuth2AuthenticationToken) {

            logger.info { "<onAuthenticationSuccess> found OAuth2AuthenticationToken: $authentication" }

            val client: OAuth2AuthorizedClient = clientService.loadAuthorizedClient(
                authentication.authorizedClientRegistrationId,
                authentication.principal.name,
            )
            val accessToken = client.accessToken.tokenValue
            val refreshToken = client.refreshToken?.tokenValue

            val federatedClient = client.clientRegistration.registrationId
            val federatedId = authentication.principal.attributes["sub"] as String
            val email = authentication.principal.attributes["email"] as String

            val federatedUser = authUserDetailsService.createFederatedUser(email, federatedClient, federatedId)

            tokenService.archive(
                authUserDetails = federatedUser,
                idToken = (authentication.principal as OidcUser) .idToken,
                accessToken = client.accessToken,
                refreshToken = client.refreshToken,
            )
        }
        delegate.onAuthenticationSuccess(request, response, authentication)
    }


}
