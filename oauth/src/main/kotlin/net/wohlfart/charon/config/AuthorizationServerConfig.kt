package net.wohlfart.charon.config

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
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
    //      https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
    @Bean
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        oAuth2AuthorizationService: OAuth2AuthorizationService,
        jwtDecoder: JwtDecoder,
    ): SecurityFilterChain {
        // externalized configurer
        val authorizationServerConfigurer = customAuthServerConfig()
        http.apply(authorizationServerConfigurer)
        // all endpoints only authenticated
        http.securityMatcher(authorizationServerConfigurer.endpointsMatcher)
        http.authorizeHttpRequests { authorizeRequests: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
            ->
            authorizeRequests.anyRequest().authenticated()
        }
        // we post from everywhere
        http.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity>
            ->
            csrfConfigurer.ignoringRequestMatchers(*arrayOf(authorizationServerConfigurer.endpointsMatcher))
        }
        // this picks up our default cors config
        http.cors { }
        // redirect to login page for any authentication issue
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.authenticationEntryPoint(
                LoginUrlAuthenticationEntryPoint(
                    DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL
                )
            )
        }
        // use access tokens for User Info and/or Client Registration
        http.oauth2ResourceServer { oAuth2ResourceServerConfigurer: OAuth2ResourceServerConfigurer<HttpSecurity>
            ->
            oAuth2ResourceServerConfigurer.jwt()
        }
        return http.build()
    }

    // this is what happens in
    // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
    private fun customAuthServerConfig(): OAuth2AuthorizationServerConfigurer {
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        authorizationServerConfigurer
            // OpenID Connect 1.0 is disabled in the default configuration, we need to enable it
            .oidc(Customizer.withDefaults())
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

}
