package net.wohlfart.charon.exception

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDate

private val logger = KotlinLogging.logger {}


// https://zetcode.com/springboot/controlleradvice/

// @ControllerAdvice
class DefaultExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(
        exception: RuntimeException,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error(exception)
        val model: MutableMap<String, Any> = LinkedHashMap()
        model["timestamp"] = LocalDate.now()
        return ResponseEntity(model, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
