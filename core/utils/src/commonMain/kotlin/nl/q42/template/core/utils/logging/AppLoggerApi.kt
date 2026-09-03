package nl.q42.template.core.utils.logging

/**
 * Pure-Kotlin logging facade. Deliberately has no dependency on any concrete logging SDK (e.g.
 * Datadog) so common code — and its unit tests — never need one either. Platform entry points
 * install a real implementation via [setAppLogger].
 */
interface AppLoggerApi {
    fun debug(message: String, throwable: Throwable? = null)
    fun info(message: String, throwable: Throwable? = null)
    fun warn(message: String, throwable: Throwable? = null)
    fun error(message: String, throwable: Throwable? = null)
}

private object NoOpAppLogger : AppLoggerApi {
    override fun debug(message: String, throwable: Throwable?) = Unit
    override fun info(message: String, throwable: Throwable?) = Unit
    override fun warn(message: String, throwable: Throwable?) = Unit
    override fun error(message: String, throwable: Throwable?) = Unit
}

/**
 * The single, app-wide logger instance. Defaults to a no-op so code that logs before bootstrap —
 * unit tests, in particular — doesn't need a real logging SDK. Set once via [setAppLogger], as
 * early as possible in the platform application lifecycle.
 */
var AppLogger: AppLoggerApi = NoOpAppLogger
    private set

fun setAppLogger(logger: AppLoggerApi) {
    AppLogger = logger
}
