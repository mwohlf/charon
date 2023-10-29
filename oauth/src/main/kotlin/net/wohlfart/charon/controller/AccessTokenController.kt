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
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class AccessTokenController(
    val tokenService: TokenService,
    val authUserDetailsService: AuthUserDetailsService,
) : AccessTokenApi {

    override fun readAccessToken(@Parameter(description = "id of the user", required = true) @PathVariable("xid") xid: String): ResponseEntity<AccessToken> {
        logger.info { "returning access token" }
        authUserDetailsService.findByXid(xid)?.let { authUserDetails ->
            tokenService.findAccessToken(authUserDetails).let { externalTokens ->
                when (externalTokens.size) {
                    0 -> {
                        logger.warn { "no token found in backend for xid='${xid}', returning 404 " }
                        return ResponseEntity.notFound().build()
                    }

                    1 -> {
                        val externalToken = externalTokens.first()
                        // TODO: check if it is expired
                        return ResponseEntity.ok(
                            AccessToken(
                                tokenValue = externalToken.value,
                                issuedAt = externalToken.issuedAt?.atOffset(ZoneOffset.UTC),
                                expiredAt = externalToken.expiredAt?.atOffset(ZoneOffset.UTC),
                            )
                        )
                    }

                    else -> {
                        // need to cleanup the database
                        logger.error { "multiple token found in backend for xid='${xid}', returning 404, found: $externalTokens" }
                        return ResponseEntity.internalServerError().build()
                    }
                }
            }
        }
        return ResponseEntity.notFound().build()
    }

}
