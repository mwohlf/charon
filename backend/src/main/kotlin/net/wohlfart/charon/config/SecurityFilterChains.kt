package net.wohlfart.charon.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy


private val logger = KotlinLogging.logger(SecurityFilterChains::class.java.name)


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class SecurityFilterChains {

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {
        http.cors { } // picks up our default cors config for the token endpoint

        logger.info { "<defaultSecurityFilterChain> disable sessions" }
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .sessionAuthenticationStrategy(NullAuthenticatedSessionStrategy())

        http.oauth2ResourceServer { oauth2ResourceServer: OAuth2ResourceServerConfigurer<HttpSecurity>
            ->
            oauth2ResourceServer.jwt(Customizer.withDefaults())
        }

        // disable for the log post endpoint
        logger.info { "<defaultSecurityFilterChain> disable csrf" }
        http.csrf { csrf -> csrf.disable() }
        // needed for iframe silent refresh
        logger.info { "<defaultSecurityFilterChain> disable frame options for iframe token refresh" }
        http.headers().frameOptions().disable()

        return http.build()
    }
}
