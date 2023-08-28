package net.wohlfart.charon.controller

import net.wohlfart.charon.api.DataAccessApi
import net.wohlfart.charon.model.FitDataSource
import net.wohlfart.charon.model.RandomData
import net.wohlfart.charon.service.FitnessStoreService
import net.wohlfart.charon.service.OAuthTokenService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime


// https://developers.google.com/fit/rest/v1/get-started

@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class DataAccessController(
    val oAuthTokenService: OAuthTokenService,
    val fitnessStoreService: FitnessStoreService,
) : DataAccessApi {

    // JwtAuthenticationToken
    // [Principal=org.springframework.security.oauth2.jwt.Jwt@98838b7d,
    // Credentials=[PROTECTED],
    // Authenticated=true,
    // Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null],
    // Granted Authorities=[SCOPE_openid, SCOPE_profile, SCOPE_offline_access, SCOPE_email]]
    @Secured(value = ["SCOPE_profile"])
    override fun readRandomData(): ResponseEntity<RandomData> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication) .block()
        return ResponseEntity.ok(
            RandomData(
                value = "the token value: ${accessToken?.tokenValue}",
                expire = accessToken?.expiredAt ?: OffsetDateTime.now(),
            )
        )
    }

    @Secured(value = ["SCOPE_profile"])
    override fun readFitDataSources(): ResponseEntity<List<FitDataSource>> {
        val accessToken = oAuthTokenService.getFitAccessToken(SecurityContextHolder.getContext().authentication) .block()
        accessToken?.let {token ->
            return ResponseEntity.ok(fitnessStoreService.fetchDataSources(token))
        }
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

}
