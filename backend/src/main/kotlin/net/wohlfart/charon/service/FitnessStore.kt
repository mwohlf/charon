package net.wohlfart.charon.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import net.wohlfart.charon.model.AccessToken
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


// https://developers.google.com/fit/rest/v1/get-started

private val logger = KotlinLogging.logger {}
var mapper = ObjectMapper()


@Service
class FitnessStore {


    fun fetchData(accessToken: AccessToken) : JsonNode {

        // https://www.googleapis.com/fitness/v1/resourcePath?parameters

        val string = WebClient.builder()
            .baseUrl("https://www.googleapis.com/fitness/v1/users/me/dataSources")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")
            .build()
            .get()
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        val result = mapper.readTree(string)

        val pretty = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        logger.error { "pretty: $pretty" }

        return result

    }

}
