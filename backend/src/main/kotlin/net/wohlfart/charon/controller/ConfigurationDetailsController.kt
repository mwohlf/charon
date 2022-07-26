package net.wohlfart.charon.controller

import net.wohlfart.charon.CharonProperties
import net.wohlfart.charon.api.ConfigApi
import net.wohlfart.charon.model.ConfigurationDetails
import org.springframework.boot.info.BuildProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.time.ZoneId


@RestController
class ConfigurationDetailsController(
    private val buildProperties: BuildProperties,
    private val charonProperties: CharonProperties,
) : ConfigApi {

    override fun readConfigurationDetails(): ResponseEntity<ConfigurationDetails> {
        return ResponseEntity.ok(ConfigurationDetails(
            name = buildProperties.name,
            version = buildProperties.version,
            timestamp = OffsetDateTime.ofInstant(buildProperties.time, ZoneId.systemDefault()) ,
        ))
    }
}
