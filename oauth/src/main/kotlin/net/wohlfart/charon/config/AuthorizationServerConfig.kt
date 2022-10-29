package net.wohlfart.charon.config

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.component.LoginCustomizer
import net.wohlfart.charon.component.LogoutCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.lang.Nullable
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenRevocationEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationConverter
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest


private val logger = KotlinLogging.logger {}

// docs:
// https://stackoverflow.com/questions/71479250/spring-security-oauth2-authorization-server-angular-auth-oidc-client
// working sample
// https://github.com/sjohnr/spring-authorization-server/tree/bff-demo/samples/default-authorizationserver/src
// implementation might have changed here according to
// https://github.com/spring-projects/spring-authorization-server/blob/main/samples/default-authorizationserver/src/main/java/sample/config/AuthorizationServerConfig.java
// more info:
// https://github.com/spring-projects/spring-authorization-server/blob/d39cc7ca7580bdcd9cbf8bd65b54c84f1dfe42e7/docs/src/docs/asciidoc/configuration-model.adoc
// or:
// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-userinfo.html
// https://github.com/spring-projects/spring-authorization-server/commit/a846e936e9a5b72567058b5b96f69aa383bc2a4a#diff-7181a8eb33d010003aa5b960b53c8c41a2eab2e43e19d1fe66e5e7ece60a53ff
@Configuration
class AuthorizationServerConfig(
    val oauthProperties: OAuthProperties,
) {

    /*
        @Throws(Exception::class)
        fun applyDefaultSecurity(http: HttpSecurity) {
            val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
            val endpointsMatcher = authorizationServerConfigurer.endpointsMatcher
            http.requestMatcher(endpointsMatcher)
                .authorizeRequests(Customizer { authorizeRequests: ExpressionInterceptUrlRegistry -> (authorizeRequests.anyRequest() as ExpressionUrlAuthorizationConfigurer.AuthorizedUrl).authenticated() })
                .csrf { csrf: CsrfConfigurer<HttpSecurity?> ->
                    csrf.ignoringRequestMatchers(
                        *arrayOf(endpointsMatcher)
                    )
                }.apply(authorizationServerConfigurer)
        }
    */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        loginCustomizer: LoginCustomizer,
        logoutCustomizer: LogoutCustomizer,
    ): SecurityFilterChain {

        // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        val endpointsMatcher = authorizationServerConfigurer.endpointsMatcher
        http.requestMatcher(endpointsMatcher)
            .authorizeRequests { authorizeRequests: ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry
                ->
                authorizeRequests
                    .antMatchers("/oauth2/revoke").permitAll()
                    .anyRequest().authenticated()
            }
            .csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity?>
                ->
                csrfConfigurer.ignoringRequestMatchers(*arrayOf(endpointsMatcher)
                )
            }.apply(authorizationServerConfigurer)
        // token endpoint
        // val authorizationServerConfigurer = http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
        // val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        // http.apply(authorizationServerConfigurer)


        // Enable OpenID Connect 1.0
        authorizationServerConfigurer
            .oidc(Customizer.withDefaults())
            // TODO: .tokenRevocationEndpoint(Customizer.withDefaults())
            // https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html#oauth2-token-revocation-endpoint
            .tokenRevocationEndpoint { oAuth2TokenRevocationEndpointConfigurer: OAuth2TokenRevocationEndpointConfigurer ->
                oAuth2TokenRevocationEndpointConfigurer
                    .revocationRequestConverter(RevokeAuthenticationConverter())
                    .authenticationProvider(RevokeAuthenticationProvider())
                    /*
                    .revocationResponseHandler { request, response, authentication
                        ->
                        /* delete session here... */
                        logger.error { "inside revocationResponseHandler" }
                        logger.info { "request: $request" }
                        logger.info { "authentication: $authentication" }
                        response.status = HttpStatus.OK.value()
                    }
                    */
                //.errorResponseHandler(errorResponseHandler)

            }

        // picks up our default cors config
        http.cors { }
        // http.oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
        }
        // for refresh inline frame needed ?
        http.headers().frameOptions().sameOrigin()

        /*
        http
            .cors { }
            // .exceptionHandling(Customizer<ExceptionHandlingConfigurer<HttpSecurity>> {
            //     fun customize(t: ExceptionHandlingConfigurer<HttpSecurity>) {
            //        t.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
            //    }
            //})
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)
            .formLogin(loginCustomizer)
            .logout(logoutCustomizer)
            .build()
        */
        // needed according to https://docs.spring.io/spring-authorization-server/docs/current/reference/html/protocol-endpoints.html
        // for the oidc endpoint user info whatever
        // val authorizationServerConfigurer: OAuth2AuthorizationServerConfigurer<HttpSecurity> =
        //    OAuth2AuthorizationServerConfigurer()
        // http.apply(authorizationServerConfigurer)
        // http.oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)

        // creates
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

    class RevokeAuthenticationConverter : AuthenticationConverter {
        @Nullable
        override fun convert(request: HttpServletRequest): Authentication {
            logger.error { "authentication: $request" }
            return TestingAuthenticationToken("test", "user")
        }
    }

    class RevokeAuthenticationProvider : AuthenticationProvider {
        override fun authenticate(authentication: Authentication): Authentication? {
            logger.error { "authentication: $authentication" }
            if (authentication is TestingAuthenticationToken) {
                authentication.isAuthenticated = true
            }
            return authentication
        }

        override fun supports(authentication: Class<*>?): Boolean {
            logger.error { "can authenticate: $authentication" }
            return true
        }

    }

}
