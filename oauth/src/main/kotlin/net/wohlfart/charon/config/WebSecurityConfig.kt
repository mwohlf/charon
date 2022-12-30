package net.wohlfart.charon.config

import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


private val logger = KotlinLogging.logger(AuthorizationServerConfig::class.java.name)


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class WebSecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        oAuthProperties: OAuthProperties,
    ): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
                ->
                authorize.anyRequest().authenticated()
            }

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)

        http.cors { } // picks up our default cors config for the token endpoint
        http.formLogin(withDefaults())
        // http.formLogin(loginCustomizer)

        http.logout().logoutUrl("/logout").permitAll().clearAuthentication(true)
            .logoutSuccessUrl(oAuthProperties.postLogoutRedirect)

        http.csrf { csrf -> csrf.disable() } // for the h2 console
        http.headers().frameOptions().sameOrigin() // which uses frames it seems

        return http.build()
    }
}
