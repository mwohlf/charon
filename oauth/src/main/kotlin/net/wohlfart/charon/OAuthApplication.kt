package net.wohlfart.charon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication


// https://docs.spring.io/spring-authorization-server/docs/current/reference/html/overview.html#feature-list

@SpringBootApplication
@EnableConfigurationProperties(OAuthProperties::class)
class OAuthApplication

fun main(args: Array<String>) {
    runApplication<OAuthApplication>(*args)
}

