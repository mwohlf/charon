package net.wohlfart.charon.config

import net.wohlfart.charon.component.LoginCustomizer
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
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
    ): SecurityFilterChain {

        // info about where static resources are served from:
        // https://spring.io/blog/2017/09/15/security-changes-in-spring-boot-2-0-m4
        http.authorizeRequests { authorizeRequests ->
            authorizeRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .mvcMatchers("/error").permitAll()
                .mvcMatchers("/logout").permitAll()
                .mvcMatchers("/login").permitAll()
                .antMatchers("/h2/**").permitAll()
               // .mvcMatchers("/revoke").permitAll()
               // .mvcMatchers("/oauth2/revoke").anonymous()
                .anyRequest().authenticated()
        }
        http.cors { } // picks up our default cors config
        http.csrf { csrf -> csrf.disable() } // for the h2 console
        http.headers().frameOptions().sameOrigin() // which uses frames it seems
        http.formLogin(loginCustomizer)
        return http.build()

    }

}
