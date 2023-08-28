package net.wohlfart.charon.service

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import net.wohlfart.charon.model.AccessToken
import org.springframework.http.HttpMethod
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.reactive.function.client.WebClient
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
            .retrieve()
            .bodyToMono(AccessToken::class.java)
    }

}
