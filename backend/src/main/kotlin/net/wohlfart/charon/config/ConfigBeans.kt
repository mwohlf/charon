package net.wohlfart.charon.config

import com.fasterxml.jackson.module.kotlin.kotlinModule
import net.wohlfart.charon.CharonProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.reactive.function.client.WebClient


@Configuration(proxyBeanMethods = false)
class ConfigBeans(
    val charonProperties: CharonProperties,
    ) {

    @Bean("tokenWebClientBuilder")
    fun getTokenWebClientBuilder(): WebClient.Builder {
        return WebClient.builder()
            .baseUrl(charonProperties.externalTokenUri)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    }

    @Bean // we need to customize for kotlin: https://github.com/FasterXML/jackson-module-kotlin
    fun jackson2ObjectMapperBuilder(): Jackson2ObjectMapperBuilder {
        return Jackson2ObjectMapperBuilder()
            .modulesToInstall(kotlinModule())
    }
}
