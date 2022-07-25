package net.wohlfart.charon.controller

import net.wohlfart.charon.api.ConfigApi
import net.wohlfart.charon.model.ConfigurationDetails
import org.springframework.boot.info.BuildProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController


@RestController
class BuildPropertiesController(
    private val buildProperties: BuildProperties,
) : ConfigApi {

    override fun readConfigurationDetails(): ResponseEntity<ConfigurationDetails> {
        return ResponseEntity.ok(ConfigurationDetails(
            date = "date",
            version = "version",
            // buildProperties.name,
        ))
    }
}
