package net.wohlfart.charon.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
class DefaultSecurityConfig {

    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        // config.addAllowedOrigin("*")
        config.addAllowedOrigin("http://127.0.0.1:4200")
        config.addAllowedOrigin("http://localhost:4200")
        // config.allowCredentials = true
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors().configurationSource(corsConfigurationSource()).and()
            .authorizeRequests()
//                .antMatchers("/.well-known/**", "/oauth2/**", "/userinfo")
            .antMatchers("/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/login")
            .authenticated()
            .and()
            .formLogin(withDefaults());

        return http.cors().configurationSource(corsConfigurationSource()).and().build();
    }

}
