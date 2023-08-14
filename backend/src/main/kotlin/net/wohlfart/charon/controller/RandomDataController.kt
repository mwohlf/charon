package net.wohlfart.charon.controller

import net.wohlfart.charon.api.RandomDataApi
import net.wohlfart.charon.model.RandomData
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.OffsetDateTime
import java.time.ZoneOffset





@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class RandomDataController() : RandomDataApi {

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
        return ResponseEntity.ok(
            RandomData(
                value = "the token string for sub ${principal.claims["sub"]} in ${principal.claims["sub"]}",
                expire = expiresAt,
            )
        )
    }

}