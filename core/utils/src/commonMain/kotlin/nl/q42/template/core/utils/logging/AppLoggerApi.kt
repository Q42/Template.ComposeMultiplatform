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

private object ConsoleAppLogger : AppLoggerApi {
    override fun debug(message: String, throwable: Throwable?) = log("DEBUG", message, throwable)
    override fun info(message: String, throwable: Throwable?) = log("INFO", message, throwable)
    override fun warn(message: String, throwable: Throwable?) = log("WARN", message, throwable)
    override fun error(message: String, throwable: Throwable?) = log("ERROR", message, throwable)

    private fun log(level: String, message: String, throwable: Throwable?) {
        println("[$level] $message")
        throwable?.printStackTrace()
    }
}

/**
 * The single, app-wide logger instance. Defaults to a simple console logger — so logs are never
 * silently lost (in unit tests, in particular, which never call [setAppLogger]) — rather than a
 * no-op. Set once via [setAppLogger], as early as possible in the platform application lifecycle.
 */
var AppLogger: AppLoggerApi = ConsoleAppLogger
    private set

fun setAppLogger(logger: AppLoggerApi) {
    AppLogger = logger
}
