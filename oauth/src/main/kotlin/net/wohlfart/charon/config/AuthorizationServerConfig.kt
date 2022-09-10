package net.wohlfart.charon.config

import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain


// docs:
// https://stackoverflow.com/questions/71479250/spring-security-oauth2-authorization-server-angular-auth-oidc-client
// working sample
// https://github.com/sjohnr/spring-authorization-server/tree/bff-demo/samples/default-authorizationserver/src

@Configuration
class AuthorizationServerConfig {


    // see: https://github.com/spring-projects/spring-authorization-server/blob/d39cc7ca7580bdcd9cbf8bd65b54c84f1dfe42e7/docs/src/docs/asciidoc/configuration-model.adoc

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {

        // https://docs.spring.io/spring-authorization-server/docs/current/reference/html/guides/how-to-userinfo.html
        // token endpoint

        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        //authorizationServerConfigurer.tokenRevocationEndpoint(tokenRevocationEndpoint -> { })

        // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.apply(authorizationServerConfigurer)
        http.cors { }
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer<HttpSecurity>::jwt)
        http.formLogin(withDefaults())   // implements the login form
        return http.build()
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

}
