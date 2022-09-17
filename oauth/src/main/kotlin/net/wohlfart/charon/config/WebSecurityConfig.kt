package net.wohlfart.charon.config

import net.wohlfart.charon.component.HeaderCustomizer
import net.wohlfart.charon.component.LoginCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService,
        loginCustomizer: LoginCustomizer,
        headerCustomizer: HeaderCustomizer,
    ): SecurityFilterChain {

        // used for:
        //   http://127.0.0.1:8081/oauth2/token
        //   http://127.0.0.1:8081/oauth2/revoke (?)
        return http
            .cors { }
            .userDetailsService(userDetailsService)
            .formLogin(loginCustomizer)
            .headers(headerCustomizer)
            .build()
    }

}
