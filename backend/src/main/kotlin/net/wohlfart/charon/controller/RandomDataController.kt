package net.wohlfart.charon.controller

import net.wohlfart.charon.api.RandomDataApi
import net.wohlfart.charon.model.RandomData
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.OffsetDateTime


@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class RandomDataController() : RandomDataApi {

    override fun readRandomData(): ResponseEntity<RandomData> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val request = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request
        return ResponseEntity.ok(
            RandomData(
                value = "the token string for $principal in $request",
                expire = OffsetDateTime.now().plusDays(2),
            )
        )
    }

}
