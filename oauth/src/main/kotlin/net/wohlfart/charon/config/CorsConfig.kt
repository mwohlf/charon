package net.wohlfart.charon.config;

import net.wohlfart.charon.OAuthProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class CorsConfig(
    val oauthProperties: OAuthProperties,
) {


    // see: https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = oauthProperties.allowedOrigins.toList()
        configuration.allowedMethods = HttpMethod.values().map { it.name }.toList()
        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", configuration)
        }
    }

}
