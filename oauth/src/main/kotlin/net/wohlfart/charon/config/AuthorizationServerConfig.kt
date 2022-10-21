package net.wohlfart.charon.config

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.component.LoginCustomizer
import net.wohlfart.charon.component.LogoutCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint


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

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        loginCustomizer: LoginCustomizer,
        logoutCustomizer: LogoutCustomizer,
    ): SecurityFilterChain {

        // token endpoint
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        // Enable OpenID Connect 1.0
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
            // TODO: .tokenRevocationEndpoint(Customizer.withDefaults())
        // picks up our default cors config
        http.cors { }
        http.exceptionHandling { exceptions ->
            exceptions.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
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

}
