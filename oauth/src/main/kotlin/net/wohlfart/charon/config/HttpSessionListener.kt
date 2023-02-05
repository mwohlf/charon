package net.wohlfart.charon.config

import jakarta.servlet.http.HttpSession
import jakarta.servlet.http.HttpSessionEvent
import jakarta.servlet.http.HttpSessionListener
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.concurrent.thread


private val logger = KotlinLogging.logger(AuthorizationServerConfig::class.java.name)

private val sessions: MutableMap<String, HttpSession> = HashMap()

@Configuration
class HttpSessionConfig {

    init {
        thread {
            while (true) {
                logger.info { "${Thread.currentThread()} has run." }
                sessions.forEach { (key, value) ->
                    logger.info { "$key = $value" }
                }
                Thread.sleep(7_000)
            }
        }
    }

    @Bean
    fun httpSessionListener(): HttpSessionListener {
        return object : HttpSessionListener {
            override fun sessionCreated(httpSessionEvent: HttpSessionEvent) {
                logger.error { "<sessionCreated> ${httpSessionEvent.session.id}" }
                sessions[httpSessionEvent.session.id] = httpSessionEvent.session
                sessions.forEach { (key, value) ->
                    logger.error { "$key = $value" }
                }
            }

            override fun sessionDestroyed(httpSessionEvent: HttpSessionEvent) {
                logger.error { "<sessionDestroyed> ${httpSessionEvent.session.id}" }
                sessions.forEach { (key, value) ->
                    logger.error { "$key = $value" }
                }
                sessions.remove(httpSessionEvent.session.id)
            }
        }
    }

}
