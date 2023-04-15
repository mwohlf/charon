package net.wohlfart.charon.config

import net.wohlfart.charon.OAuthProperties
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class WebSecurityConfig {

    /*
    // TODO: move this somewhere else
    @Bean
    fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder? {
        return Jackson2ObjectMapperBuilder()
            //.mixIn()
    }*/

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        oAuthProperties: OAuthProperties,
    ): SecurityFilterChain {
        // serve web resources, and protect anything else
        http.authorizeHttpRequests { authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                authorize
                    // our web resources should be available without authentication
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers("/confirm").permitAll()
                    .requestMatchers("/error").permitAll()
                    .requestMatchers("/home").permitAll()
                    .requestMatchers("/register").permitAll()
                    // .requestMatchers("/h2/**").permitAll()
                    // anything authenticated is fine
                    .anyRequest().authenticated()
            }
        // use our global cors config
        http.cors { } // picks up our default cors config for the token endpoint
        // customized form login
        http.formLogin()
            .loginPage(DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL)
            .permitAll()
        // send back to applicaion on logout
        http.logout().logoutSuccessUrl(oAuthProperties.appHomeUrl)
        // ths is for the H2 console TODO: not for production
        http.csrf { csrf -> csrf.disable() } // for the h2 console
        http.headers().frameOptions().sameOrigin() // which uses frames it seems
        return http.build()
    }
}
