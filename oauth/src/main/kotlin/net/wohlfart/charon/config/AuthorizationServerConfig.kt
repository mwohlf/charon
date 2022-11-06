package net.wohlfart.charon.config

import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenRevocationEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import java.text.ParseException


private val logger = KotlinLogging.logger {}


@Configuration
class AuthorizationServerConfig(
    val oauthProperties: OAuthProperties,
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {

        // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        // Enable OpenID Connect 1.0
        authorizationServerConfigurer
            .oidc(Customizer.withDefaults())
            .tokenRevocationEndpoint { oAuth2TokenRevocationEndpointConfigurer: OAuth2TokenRevocationEndpointConfigurer ->
                oAuth2TokenRevocationEndpointConfigurer
                    .authenticationProvider(RevokeAuthenticationProvider())
                //    .revocationRequestConverter(RevokeAuthenticationConverter())
            }

        val endpointsMatcher = authorizationServerConfigurer.endpointsMatcher

        http.requestMatcher(endpointsMatcher)
            .authorizeRequests { authorizeRequests: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                ->
                authorizeRequests
                    .antMatchers("/oauth2/revoke").permitAll()
                    .anyRequest().authenticated()
            }

        http.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity>
            ->
            csrfConfigurer.ignoringRequestMatchers(
                *arrayOf(endpointsMatcher)
            )
        }

        http.apply(authorizationServerConfigurer)

        // picks up our default cors config
        http.cors { }
        // http.oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
        }
        // for refresh inline frame needed ?
        http.headers().frameOptions().sameOrigin()

        return http.build()
    }


    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder()
            .issuer(oauthProperties.issuer)
            // configure endpoints here that will be used in the well-known...
            .build()
    }

    /*
    class RevokeAuthenticationConverter : AuthenticationConverter {
        @Nullable
        override fun convert(request: HttpServletRequest): Authentication {
            logger.error { "authentication: $request" }
            return TestingAuthenticationToken("test", "user")
        }
    }


    class RevokeAuthenticationData(request: HttpServletRequest) : PreAuthenticatedAuthenticationToken(null, request)

    // invoked first
    class RevokeAuthenticationConverter : AuthenticationConverter {
        override fun convert(request: HttpServletRequest): Authentication {
            request.logout();
            return RevokeAuthenticationData(request)
        }
    }

     */


    // invoked second
    class RevokeAuthenticationProvider() : AuthenticationProvider {
        override fun authenticate(authentication: Authentication): Authentication {
            if (authentication is OAuth2TokenRevocationAuthenticationToken) {
                try {
                    val jwsObject = JWSObject.parse(authentication.token)
                    val subscriber = jwsObject.payload.toJSONObject()["sub"]
                    logger.error { "logout subscriber: $subscriber" }
                } catch (ex: ParseException) {
                    logger.error { "ex: $ex" }
                }
                authentication.isAuthenticated = true
            }
            return authentication
        }

        override fun supports(authentication: Class<*>): Boolean {
            return (authentication == OAuth2TokenRevocationAuthenticationToken::class.java)
        }

    }

}
