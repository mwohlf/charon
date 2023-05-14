package net.wohlfart.charon.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy


private val logger = KotlinLogging.logger(SecurityFilterChains::class.java.name)


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class SecurityFilterChains {

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {

        logger.info { "<defaultSecurityFilterChain> disable sessions" }
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .sessionAuthenticationStrategy(NullAuthenticatedSessionStrategy())

        // disable for the log post endpoint
        logger.info { "<defaultSecurityFilterChain> disable csrf" }
        http.csrf { csrf -> csrf.disable() }
        // needed for iframe silent refresh
        logger.info { "<defaultSecurityFilterChain> disable frame options for iframe token refresh" }
        http.headers().frameOptions().disable()

        return http.build()
    }
}
