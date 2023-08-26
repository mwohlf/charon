package net.wohlfart.charon.controller

import io.swagger.v3.oas.annotations.Parameter
import mu.KotlinLogging
import net.wohlfart.charon.api.AccessTokenApi
import net.wohlfart.charon.model.AccessToken
import net.wohlfart.charon.service.AuthUserDetailsService
import net.wohlfart.charon.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class AccessTokenController(
    val tokenService: TokenService,
    val authUserDetailsService: AuthUserDetailsService,
) : AccessTokenApi {

    override fun readAccessToken(@Parameter(description = "id of the user", required = true) @PathVariable("xid") xid: String): ResponseEntity<AccessToken> {
        logger.info { "returning access token" }
        authUserDetailsService.findByXid(xid)?.let {authUserDetails ->
            tokenService.findAccessToken(authUserDetails).let {externalTokens ->
                // TODO move the complexity into the query, just return the right token from the repository
                val externalToken = externalTokens.first()
                return ResponseEntity.ok(AccessToken(
                    tokenValue = externalToken.value,
                    issuedAt = OffsetDateTime.now(),
                    expiredAt = OffsetDateTime.now().plusWeeks(3),
                ))
            }
        }
        throw IllegalStateException("can't find token")
    }

}
