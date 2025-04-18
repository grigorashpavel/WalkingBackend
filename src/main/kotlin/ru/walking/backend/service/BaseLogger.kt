package ru.walking.backend.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class BaseLogger {

    private val walkingLogger: Logger = LoggerFactory.getLogger(javaClass)

    protected fun logDebug(message: String) = walkingLogger.debug(message)

    protected fun logInfo(message: String) = walkingLogger.info(message)

    protected fun logWarn(message: String) = walkingLogger.warn(message)

    protected fun logError(message: String, ex: Exception? = null) = walkingLogger.error(message, ex)
}
