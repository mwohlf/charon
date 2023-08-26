package net.wohlfart.charon.controller

import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import mu.KotlinLogging
import net.wohlfart.charon.CharonProperties
import net.wohlfart.charon.api.LoggingApi
import net.wohlfart.charon.model.LogEntry
import org.springframework.boot.info.BuildProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("\${net.wohlfart.charon.api.logging-path}")
class LoggingController(
    private val buildProperties: BuildProperties,
    private val charonProperties: CharonProperties,
) : LoggingApi {

    init {
        logger.info { "boot up ${buildProperties.name}, version ${buildProperties.version} waiting for incoming logs from frontend"}
        logger.info { "logger is listening at ${charonProperties.api.loggingPath}"}
    }

    override fun postLogRecord(@Parameter(description = "", required = true) @Valid @RequestBody logEntry: LogEntry): ResponseEntity<Unit> {
        logger.info { logEntry }
        /*
           {
                "level":5,
                "additional":["{}"],
                "message":"<logoutAction> could not find the configuration for :",
                "timestamp":"2023-02-25T21:15:19.596Z",
                "fileName":"main.js",
                "lineNumber":2191,
                "columnNumber":22
            }
        */
        return ResponseEntity(HttpStatus.OK)
    }

}


