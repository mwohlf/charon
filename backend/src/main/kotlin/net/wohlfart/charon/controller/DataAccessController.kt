package net.wohlfart.charon.controller

import net.wohlfart.charon.api.DataAccessApi
import net.wohlfart.charon.model.AccessToken
import net.wohlfart.charon.model.ProtectedData
import net.wohlfart.charon.service.FitnessStoreService
import net.wohlfart.charon.service.OAuthTokenService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant


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
    override fun readProtectedData(): ResponseEntity<ProtectedData> {
        // get the access token
        val accessToken : AccessToken? = oAuthTokenService
            .getFitAccessToken(SecurityContextHolder.getContext().authentication)
            .block()


        return ResponseEntity.ok(
            ProtectedData(
                value = "the token value: ${accessToken?.tokenValue}",
                expire = accessToken?.expiredAt ?: Instant.now(),
            )
        )
    }

}
