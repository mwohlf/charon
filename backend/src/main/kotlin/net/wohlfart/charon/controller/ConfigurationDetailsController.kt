package net.wohlfart.charon.controller

import net.wohlfart.charon.CharonProperties
import net.wohlfart.charon.api.ConfigurationDetailsApi
import net.wohlfart.charon.model.ClientConfiguration
import net.wohlfart.charon.model.ConfigurationDetails
import org.springframework.boot.info.BuildProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.time.ZoneId


@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class ConfigurationDetailsController(
    private val buildProperties: BuildProperties,
    private val charonProperties: CharonProperties,
) : ConfigurationDetailsApi {

    override fun readConfigurationDetails(): ResponseEntity<ConfigurationDetails> {
        return ResponseEntity.ok(
            ConfigurationDetails(
                name = buildProperties.name,
                version = buildProperties.version,
                timestamp = OffsetDateTime.ofInstant(buildProperties.time, ZoneId.systemDefault()),
                logging = charonProperties.api.logging,
            )
        )
    }

    override fun readClientConfigurationList(): ResponseEntity<List<ClientConfiguration>> {
        return ResponseEntity.ok(charonProperties.oauthClients.map {
            ClientConfiguration(
                configId = it.configId,
                clientId = it.clientId,
                issuerUri = it.issuerUri,
            )
        })
        /*
        configId: SIMPLE_CONFIG,
        authority: 'http://127.0.0.1:8081',
        redirectUrl: 'http://127.0.0.1:4200/home',
        postLogoutRedirectUri: window.location.origin,
        clientId: 'public-client',
        scope: 'openid profile email offline_access',
        responseType: 'code',
        silentRenew: true,
        useRefreshToken: true,
        logLevel: LogLevel.Debug,
        autoUserInfo: false,

        secureRoutes: [
          '/api',
          '/oauth2',
          'https://localhost/',
          'https://127.0.0.1/',
          'https://localhost:8080/',
          'https://127.0.0.1:8080/',
          'https://localhost:4200/',
          'https://127.0.0.1:4200/',
          'http://127.0.0.1:8081/oauth2/revoke',
        ],
        val result = listOf(
            ClientConfiguration(
                configId = "SimpleConfig",
                config = hashMapOf(
                    "authority" to "http://oauth.finalrestingheartrate.com/",
                    "redirectUrl" to "http://127.0.0.1:4200/home",
                    "postLogoutRedirectUri" to "http://127.0.0.1:4200/home",
                    "clientId" to "public-client",
                    "scope" to "openid profile email offline_access",
                    "responseType" to "code",
                    "silentRenew" to true,
                    "useRefreshToken" to true,
                    "autoUserInfo" to false,
                    "secureRoutes" to listOf(
                        "/api",
                        "/oauth2",
                        "https://localhost/",
                        "https://127.0.0.1/",
                        "https://localhost:8080/",
                        "https://127.0.0.1:8080/",
                        "https://localhost:4200/",
                        "https://127.0.0.1:4200/",
                        "http://127.0.0.1:8081/oauth2/revoke",
                    )
                )
            ),
        )
        return ResponseEntity.ok(result)
        */
    }

}


