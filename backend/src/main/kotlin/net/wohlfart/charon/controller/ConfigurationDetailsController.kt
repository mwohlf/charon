package net.wohlfart.charon.controller

import net.wohlfart.charon.CharonProperties
import net.wohlfart.charon.api.ConfigurationDetailsApi
import net.wohlfart.charon.model.ClientConfiguration
import net.wohlfart.charon.model.ConfigurationDetails
import org.springframework.boot.info.BuildProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class ConfigurationDetailsController(
    private val buildProperties: BuildProperties,
    private val charonProperties: CharonProperties,
) : ConfigurationDetailsApi {

    // app configs, should be readable for everyone
    override fun readConfigurationDetails(): ResponseEntity<ConfigurationDetails> {
        return ResponseEntity.ok(
            ConfigurationDetails(
                name = buildProperties.name,
                version = buildProperties.version,
                timestamp = buildProperties.time,
                logging = charonProperties.api.loggingPath,
            )
        )
    }

    // only for authenticated users
    override fun readClientConfigurationList(): ResponseEntity<List<ClientConfiguration>> {
        return ResponseEntity.ok(charonProperties.oauthClients.map {
            ClientConfiguration(
                configId = it.configId,
                clientId = it.clientId,
                issuerUri = it.issuerUri,
                postLogoutRedirectUri = it.postLogoutRedirectUri,
            )
        })
    }

}


