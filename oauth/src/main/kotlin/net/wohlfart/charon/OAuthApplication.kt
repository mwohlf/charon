package net.wohlfart.charon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder


// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/overview.html#feature-list

@SpringBootApplication
@EnableConfigurationProperties(OAuthProperties::class)
class OAuthApplication

fun main(args: Array<String>) {
    runApplication<OAuthApplication>(*args)
}

