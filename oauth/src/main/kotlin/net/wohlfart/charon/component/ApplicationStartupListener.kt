package net.wohlfart.charon.component

import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class ApplicationStartupListener(
    val oauthProperties: OAuthProperties,
    val mailSender: JavaMailSenderImpl,
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
        oauthProperties.clients.forEach {
            logger.info { "  clientId:  ${it.key}" }
            logger.info { "     authorizationGrantType:  ${it.value.authorizationGrantType.value}" }
            logger.info { "     clientAuthenticationMethod:  ${it.value.clientAuthenticationMethod.value}" }
            logger.info { "     redirectUris:  ${it.value.redirectUris.joinToString(""",""")}" }
            logger.info { "     scopes:  ${it.value.scopes.joinToString(""",""")}" }
        }

        logger.info { "mailSender.host: ${mailSender.host}" }
        logger.info { "mailSender.username: ${mailSender.username}" }

        logger.info { "--------------------------------------------" }
    }

}
