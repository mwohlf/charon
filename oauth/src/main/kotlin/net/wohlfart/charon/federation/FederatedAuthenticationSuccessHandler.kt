package net.wohlfart.charon.federation

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.function.Consumer

private val logger = KotlinLogging.logger {}

@Component
class FederatedAuthenticationSuccessHandler(
    val clientService: OAuth2AuthorizedClientService
): AuthenticationSuccessHandler  {

    private val delegate: AuthenticationSuccessHandler = SavedRequestAwareAuthenticationSuccessHandler()

    private var oauth2UserHandler: Consumer<OAuth2User> = Consumer<OAuth2User> { user -> {} }

    private var oidcUserHandler: Consumer<OidcUser> = Consumer<OidcUser> { user -> oauth2UserHandler.accept(user) }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication) {
        if (authentication is OAuth2AuthenticationToken) {
            if (authentication.principal is OidcUser) {
                oidcUserHandler.accept(authentication.principal as OidcUser)
                val client: OAuth2AuthorizedClient = clientService.loadAuthorizedClient(
                    authentication.authorizedClientRegistrationId,
                    authentication.principal.name,
                )
                // client has a slot for the refresh token, but its empty...
                val accessToken = client.accessToken.tokenValue
                logger.info { "accessToken: $accessToken" }
                val refreshToken = client.refreshToken?.tokenValue ?: ""
                logger.info { "refreshToken: $refreshToken" }

            } else if (authentication.principal is OAuth2User) {
                oauth2UserHandler.accept(authentication.principal as OAuth2User)
            }
        }
        delegate.onAuthenticationSuccess(request, response, authentication)
    }

    fun setOAuth2UserHandler(oauth2UserHandler: Consumer<OAuth2User>) {
        this.oauth2UserHandler = oauth2UserHandler
    }

    fun setOidcUserHandler(oidcUserHandler: Consumer<OidcUser>) {
        this.oidcUserHandler = oidcUserHandler
    }
}
