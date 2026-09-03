package nl.q42.template.logging

import com.datadog.kmp.log.Logger
import nl.q42.template.core.utils.logging.AppLoggerApi

internal class DatadogAppLogger(private val logger: Logger) : AppLoggerApi {
    override fun debug(message: String, throwable: Throwable?) = logger.debug(message, throwable)
    override fun info(message: String, throwable: Throwable?) = logger.info(message, throwable)
    override fun warn(message: String, throwable: Throwable?) = logger.warn(message, throwable)
    override fun error(message: String, throwable: Throwable?) = logger.error(message, throwable)
}
