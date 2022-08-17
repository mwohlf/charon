package net.wohlfart.charon.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfigurationSource


@EnableWebSecurity
class DefaultSecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService,
    ): SecurityFilterChain {

        // http://127.0.0.1:8081/oauth2/revoke
        return http
            .cors { }
            .userDetailsService(userDetailsService)
            .formLogin(withDefaults())
            .build()
    }

}
