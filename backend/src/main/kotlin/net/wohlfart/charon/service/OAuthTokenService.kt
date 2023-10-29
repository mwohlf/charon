package net.wohlfart.charon.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import net.wohlfart.charon.model.AccessToken
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.time.OffsetDateTime
import java.time.ZoneOffset


private val logger = KotlinLogging.logger {}
private var mapper = ObjectMapper()

@Service
class OAuthTokenService(
    private val tokenWebClientBuilder: WebClient.Builder
) {

    fun getFitAccessToken(authentication: Authentication): Mono<AccessToken> {
        val principal = authentication.principal as Jwt
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val expiresAt: OffsetDateTime = principal.expiresAt?.atOffset(ZoneOffset.UTC)!!

        val xid = principal.claims["xid"]
        return tokenWebClientBuilder.build()
            .method(HttpMethod.GET)
            .uri("/$xid")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(
                { status: HttpStatusCode -> status.is4xxClientError },
                {
                    logger.warn { "404 from auth server, no access token available for $xid" }
                    Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND))
                })
            .bodyToMono(AccessToken::class.java)
    }

}
