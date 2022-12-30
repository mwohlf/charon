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
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType.ACCESS_TOKEN
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenRevocationEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint


private val logger = KotlinLogging.logger(AuthorizationServerConfig::class.java.name)


@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfig(
    val oauthProperties: OAuthProperties,
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        oAuth2AuthorizationService: OAuth2AuthorizationService,
    ): SecurityFilterChain {

        // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        // Enable OpenID Connect 1.0
        authorizationServerConfigurer
            .oidc(Customizer.withDefaults())
            .tokenRevocationEndpoint { oAuth2TokenRevocationEndpointConfigurer: OAuth2TokenRevocationEndpointConfigurer
                ->
                oAuth2TokenRevocationEndpointConfigurer
                    .authenticationProvider(RevokeAuthenticationProvider(oAuth2AuthorizationService))
            }

        val endpointsMatcher = authorizationServerConfigurer.endpointsMatcher

        http.securityMatcher(endpointsMatcher)
            .authorizeHttpRequests { authorizeRequests: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
                ->
                authorizeRequests
                    .requestMatchers("/oauth2/revoke").permitAll()
                    .anyRequest().authenticated()
            }

        http.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity>
            ->
            csrfConfigurer.ignoringRequestMatchers(
                *arrayOf(endpointsMatcher)
            )
        }

        http.apply(authorizationServerConfigurer)


        // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        // http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
        //    .oidc(Customizer.withDefaults()) // Enable OpenID Connect 1.0

        // this picks up our default cors config
        http.cors { }

        // redirect to login page for any exception
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
        }

        // for refresh inline frame needed ?
        // http.headers().frameOptions().sameOrigin()
        // whenever we provide any data to the app maybe?
        // http.oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }


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


    // invoked second
    class RevokeAuthenticationProvider(
        private val oAuth2AuthorizationService: OAuth2AuthorizationService
    ) : AuthenticationProvider {
        override fun authenticate(authentication: Authentication): Authentication {
            if (authentication is OAuth2TokenRevocationAuthenticationToken) {
                try {
                    authentication.token?.let { token ->
                        val jwsObject = JWSObject.parse(authentication.token)
                        val subscriber = jwsObject.payload.toJSONObject()["sub"]
                        logger.info { "logout subscriber: $subscriber" }
                        val auth = oAuth2AuthorizationService.findByToken(token, ACCESS_TOKEN)
                        auth?.let { authorization ->
                            logger.info { "auth: $authorization" }
                            oAuth2AuthorizationService.remove(authorization)
                        }
                    }

                } catch (ex: Exception) {
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
