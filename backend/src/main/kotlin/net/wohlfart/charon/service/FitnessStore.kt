package net.wohlfart.charon.service

import mu.KotlinLogging
import net.wohlfart.charon.model.AccessToken
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


// https://developers.google.com/fit/rest/v1/get-started

private val logger = KotlinLogging.logger {}

@Service
class FitnessStore {


    fun fetchData(accessToken: AccessToken) {
        /*
GET /fitness/v1/users/me/dataSources HTTP/1.1
Host: www.googleapis.com
Content-length: 0
Authorization: Bearer ya29.a0AfB_byAGyv9OBBC5S6UX1cxZ446EW09FPCzOkVrdClO1jkbX4M5qgGfezF6bNfhj0hDVYKaTGDAopFkpxVoyCiqpYmNAfpyMjjD_PRWxWcnsIDQ-x2wHe75IunYQun6EOPP7B-CK4C6I0iWf6CTFXqkdKff47EMkq0wZigaCgYKAUwSARESFQHsvYls5gSipHzDX8vDjpt26HmR0A0173

         */

        // https://www.googleapis.com/fitness/v1/resourcePath?parameters

        val result = WebClient.builder()
            .baseUrl("https://www.googleapis.com/fitness/v1/users/me/dataSources")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")
            .build()
            .get()
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        logger.error { "result: $result" }
    }

}
