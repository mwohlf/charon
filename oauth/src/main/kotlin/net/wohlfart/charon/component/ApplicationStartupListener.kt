package net.wohlfart.charon.component

import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class ApplicationStartupListener(
    val oauthProperties: OAuthProperties,
) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "" }
        logger.info { "Application Config" }
        logger.info { "------------------" }
        logger.info { "oauthProperties.dataDir: ${oauthProperties.dataDir}" }
        logger.info { "charonProperties.allowedOrigins: " }
        oauthProperties.allowedOrigins.forEach {
            logger.info { "    $it" }
        }
        logger.info { "charonProperties.issuer: ${oauthProperties.issuer}" }
        oauthProperties.clientRegistry.forEach {
            logger.info { "  clientId:  ${it.clientId}" }
            logger.info { "     authorizationGrantType:  ${it.authorizationGrantType.value}" }
            logger.info { "     clientAuthenticationMethod:  ${it.clientAuthenticationMethod.value}" }
            logger.info { "     redirectUris:  ${it.redirectUris.joinToString(""",""")}" }
            logger.info { "     scopes:  ${it.scopes.joinToString(""",""")}" }
        }
        logger.info { "--------------------------------------------" }
    }

}
