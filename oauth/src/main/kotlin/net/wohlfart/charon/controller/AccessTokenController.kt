package net.wohlfart.charon.controller

import mu.KotlinLogging
import net.wohlfart.charon.api.AccessTokenApi
import net.wohlfart.charon.model.AccessToken
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class AccessTokenController : AccessTokenApi {

    override fun readAccessToken(): ResponseEntity<AccessToken> {
        logger.info { "returning access token" }
        return ResponseEntity.ok(
            AccessToken(
                tokenValue = "bla",
                issuedAt = OffsetDateTime.now(),
                expiredAt = OffsetDateTime.now().plusWeeks(3),
            )
        )
    }

}
