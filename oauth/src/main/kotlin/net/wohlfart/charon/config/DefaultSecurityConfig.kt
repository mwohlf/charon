package net.wohlfart.charon.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class DefaultSecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
    ): SecurityFilterChain {
        return http
            .authorizeRequests { authorizeRequests -> authorizeRequests.anyRequest().authenticated() }
            .cors(withDefaults())
            .formLogin(withDefaults())
            .build()
    }

}
