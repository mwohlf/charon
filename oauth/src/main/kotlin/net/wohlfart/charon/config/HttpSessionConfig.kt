package net.wohlfart.charon.config

import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.http.HttpSession
import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener


private val logger = KotlinLogging.logger(AuthorizationServerConfig::class.java.name)

private val sessions: MutableMap<String, HttpSession> = HashMap()

@Configuration
class HttpSessionConfig {

    @Bean
    fun httpSessionListener(): HttpSessionListener {
        return object : HttpSessionListener {
            override fun sessionCreated(httpSessionEvent: HttpSessionEvent) {
                logger.error { "<sessionCreated> ${httpSessionEvent.session.id}" }
                sessions.forEach { (key, value) -> println("$key = $value") }
                sessions[httpSessionEvent.session.id] = httpSessionEvent.session
            }

            override fun sessionDestroyed(httpSessionEvent: HttpSessionEvent) {
                sessions.forEach { (key, value) -> println("$key = $value") }
                logger.error { "<sessionDestroyed> ${httpSessionEvent.session.id}" }
                sessions.remove(httpSessionEvent.session.id)
            }
        }
    }

}
