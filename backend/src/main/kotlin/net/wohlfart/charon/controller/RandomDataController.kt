package net.wohlfart.charon.controller

import net.wohlfart.charon.api.RandomDataApi
import net.wohlfart.charon.model.AccessToken
import net.wohlfart.charon.model.RandomData
import net.wohlfart.charon.service.FitnessStore
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.reactive.function.client.WebClient
import java.time.OffsetDateTime
import java.time.ZoneOffset



// https://developers.google.com/fit/rest/v1/get-started

@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class RandomDataController(
    val tokenWebClientBuilder: WebClient.Builder,
    val fitnessStore: FitnessStore,
) : RandomDataApi {

    // JwtAuthenticationToken
    // [Principal=org.springframework.security.oauth2.jwt.Jwt@98838b7d,
    // Credentials=[PROTECTED],
    // Authenticated=true,
    // Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null],
    // Granted Authorities=[SCOPE_openid, SCOPE_profile, SCOPE_offline_access, SCOPE_email]]
    @Secured(value = ["SCOPE_profile"])
    override fun readRandomData(): ResponseEntity<RandomData> {
        val principal = SecurityContextHolder.getContext().authentication.principal as Jwt
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        val expiresAt: OffsetDateTime = principal.expiresAt?.atOffset(ZoneOffset.UTC)!!

        val xid = principal.claims["xid"]
        val accessToken = tokenWebClientBuilder.build()
            .get()
            .uri("/$xid")
            .retrieve()
            .bodyToMono(AccessToken::class.java)
            .block()

        accessToken?.let {token ->
            fitnessStore.fetchData(token)
        }


        return ResponseEntity.ok(
            RandomData(
                value = "the string for xid ${principal.claims["xid"]} in principal.claims token value: ${accessToken?.tokenValue}",
                expire = expiresAt,
            )
        )
    }

}
