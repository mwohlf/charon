package net.wohlfart.charon.config

import jakarta.servlet.http.HttpSession
import jakarta.servlet.http.HttpSessionEvent
import jakarta.servlet.http.HttpSessionListener
import mu.KotlinLogging
import kotlin.concurrent.thread


private val logger = KotlinLogging.logger(HttpSessionConfig::class.java.name)

private val sessions: MutableMap<String, HttpSession> = HashMap()

//
//
//  session id is changed after login to protect from session fixation attacks
//  so this bean tracking session ids in a hash is pretty useless
//
// @Configuration
class HttpSessionConfig {

    // TODO: keeping track of sessions here doesnt work because sessions change their id during login
    // session rotation, to protected from session fixation attacks
    init {
        thread {
            while (true) {
                logger.info { "${Thread.currentThread()} has run." }
                sessions.forEach { (key, value) ->
                    logger.info { "$key = $value" }
                }
                Thread.sleep(70_000)
            }
        }
    }


    // @Bean
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
