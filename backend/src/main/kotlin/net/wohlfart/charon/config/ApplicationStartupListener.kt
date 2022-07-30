package net.wohlfart.charon.config


import mu.KotlinLogging
import net.wohlfart.charon.CharonProperties
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.info.BuildProperties
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}


@Component
class ApplicationStartupListener(
    val buildProperties: BuildProperties,
    val charonProperties: CharonProperties,
) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        logger.info { "" }
        logger.info { "Application Config" }
        logger.info { "------------------" }
        logger.info { "buildProperties.time: ${buildProperties.time}" }
        logger.info { "buildProperties.version: ${buildProperties.version}" }
        logger.info { "charonProperties.webjarBase: ${charonProperties.webjarBase}" }
        logger.info { "buildProperties.dataDir: ${charonProperties.dataDir}" }
        logger.info { "charonProperties.api.basePath: ${charonProperties.api.basePath}" }
        logger.info { "--------------------------------------------" }
    }

}
