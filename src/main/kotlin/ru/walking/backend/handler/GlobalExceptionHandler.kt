package ru.walking.backend.handler

import org.jboss.logging.MDC
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import ru.walking.backend.common.Constants

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    private val walkingLogger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val traceId = MDC.get(Constants.TRACE_ID_KEY) ?: "none"
        walkingLogger.error("Unhandled exception [traceId=$traceId]", ex)

        return ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
