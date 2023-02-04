package net.wohlfart.charon.config

import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
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
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter

private val logger = KotlinLogging.logger(AuthorizationServerConfig::class.java.name)

@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfig(
    val oauthProperties: OAuthProperties,
) {

    // see: https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

    @Bean
    // @Order(Ordered.HIGHEST_PRECEDENCE)
    // @Throws(java.lang.Exception::class)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        oAuth2AuthorizationService: OAuth2AuthorizationService,
        jwtDecoder: JwtDecoder,
    ): SecurityFilterChain {

        // externalized configuration setter
        val authorizationServerConfigurer = customAuthServerConfig(oAuth2AuthorizationService, jwtDecoder)

        // the endpoints needed by the server, thi filter will be applied to them
        http.securityMatcher(authorizationServerConfigurer.endpointsMatcher)


        http.authorizeHttpRequests { authorizeRequests: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
            ->
            authorizeRequests
                //.requestMatchers(REVOKE_ENDPOINT).permitAll()
                .anyRequest().authenticated()
        }

        http.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity>
            ->
            csrfConfigurer.ignoringRequestMatchers(
                *arrayOf(authorizationServerConfigurer.endpointsMatcher)
            )
        }

        http.apply(authorizationServerConfigurer)

        // this picks up our default cors config
        http.cors { }

        // redirect to login page for any authentication issue
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint(
                DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL
            ))
        }

        // for refresh inline frame needed ?
        // http.headers().frameOptions().sameOrigin()

        // whenever we provide any data to the app maybe?
        // http.oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity?> -> obj.jwt() }

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
        //.sessionAuthenticationStrategy(NullAuthenticatedSessionStrategy())

        return http.build()
    }

    // this is what happens in
    // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
    private fun customAuthServerConfig(
        oAuth2AuthorizationService: OAuth2AuthorizationService,
        jwtDecoder: JwtDecoder,
    ): OAuth2AuthorizationServerConfigurer {
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        authorizationServerConfigurer
            // OpenID Connect 1.0 is disabled in the default configuration
            .oidc(Customizer.withDefaults())
            // customize the revocation endpoint
            .tokenRevocationEndpoint { oAuth2TokenRevocationEndpointConfigurer: OAuth2TokenRevocationEndpointConfigurer
                ->
                oAuth2TokenRevocationEndpointConfigurer
                    .authenticationProvider(RevokeAuthenticationProvider(oAuth2AuthorizationService))
                //.authenticationProvider(JwtAuthenticationProvider(jwtDecoder))
            }
        return authorizationServerConfigurer
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
