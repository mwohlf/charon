package net.wohlfart.charon.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class CorsConfig {

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.addAllowedOrigin("http://127.0.0.1:4200")
        config.addAllowedOrigin("http://localhost:4200")
        config.addAllowedOrigin("http://127.0.0.1:8080")
        config.addAllowedOrigin("http://localhost:8080")
        config.addAllowedOrigin("http://127.0.0.1:8081")
        config.addAllowedOrigin("http://localhost:8081")
        config.allowCredentials = true
        source.registerCorsConfiguration("/**", config)
        return source
    }

}
