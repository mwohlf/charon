package net.wohlfart.charon.config

import net.wohlfart.charon.component.LoginCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer.withDefaults
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
    ): SecurityFilterChain {

        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                .antMatchers("/styles/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated()
        }
        http.cors { } // picks up our default cors config
        http.formLogin(loginCustomizer)
        return http.build();

    }

}
