package net.wohlfart.charon.controller

import net.wohlfart.charon.api.BuildApi
import org.springframework.boot.info.BuildProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController


@RestController
class BuildPropertiesController(
    private val buildProperties: BuildProperties,
) : BuildApi {

    override fun readBuildProperties(): ResponseEntity<net.wohlfart.charon.model.BuildProperties> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
