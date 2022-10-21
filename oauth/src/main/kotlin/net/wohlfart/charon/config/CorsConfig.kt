package net.wohlfart.charon.config;

import net.wohlfart.charon.OAuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


// see: https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html

@Configuration
class CorsConfig(
    val oauthProperties: OAuthProperties,
) {

    @Bean
    @Primary
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()

        configuration.allowedOrigins = oauthProperties.allowedOrigins.toList()
        // configuration.allowedOriginPatterns = listOf("*")
        configuration.allowedMethods = HttpMethod.values().map { it.name }.toList()
        // configuration.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        configuration.allowCredentials = true
        configuration.allowedHeaders = listOf("*") // TODO
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

}
