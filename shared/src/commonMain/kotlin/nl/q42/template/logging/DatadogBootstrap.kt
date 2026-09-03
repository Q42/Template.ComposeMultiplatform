package nl.q42.template.logging

import com.datadog.kmp.Datadog
import com.datadog.kmp.DatadogSite
import com.datadog.kmp.SdkLogVerbosity
import com.datadog.kmp.core.configuration.Configuration
import com.datadog.kmp.log.LogLevel
import com.datadog.kmp.log.Logger
import com.datadog.kmp.log.Logs
import com.datadog.kmp.log.configuration.LogsConfiguration
import com.datadog.kmp.privacy.TrackingConsent
import com.datadog.kmp.rum.Rum
import com.datadog.kmp.rum.configuration.RumConfiguration
import nl.q42.template.BuildKonfig
import nl.q42.template.core.utils.logging.AppLoggerApi
import nl.q42.template.core.utils.logging.setAppLogger
import nl.q42.template.di.isDebug

/**
 * Initializes the Datadog SDK (Core, Logs, RUM, crash reporting) and installs the app-wide
 * [nl.q42.template.core.utils.logging.AppLogger]. Common to both platforms since the Datadog
 * Kotlin Multiplatform SDK ships real Android/iOS actuals. Call exactly once, as early as
 * possible in the platform application lifecycle, before any logging happens.
 *
 * @param context the platform application context (required on Android, ignored on iOS)
 */
fun initializeDatadog(context: Any? = null) {
    val debug = isDebug()

    val configuration = Configuration.Builder(
        clientToken = BuildKonfig.DATADOG_CLIENT_TOKEN,
        env = if (debug) "development" else "production",
        service = BuildKonfig.DATADOG_SERVICE,
    )
        .trackCrashes(true)
        .useSite(DatadogSite.valueOf(BuildKonfig.DATADOG_SITE.uppercase()))
        .build()

    Datadog.initialize(context, configuration, TrackingConsent.GRANTED)
    Datadog.verbosity = if (debug) SdkLogVerbosity.DEBUG else null

    Logs.enable(LogsConfiguration.Builder().build())

    val logger = Logger.Builder()
        .setName("Template")
        .setPrintLogsToConsole(debug)
        .setRemoteLogThreshold(if (debug) LogLevel.DEBUG else LogLevel.INFO)
        .build()
    logger.addTag("platform", platformTag)

    setAppLogger(DatadogAppLogger(logger))

    // Crash reports are delivered as RUM errors, so RUM must be enabled for crash reporting
    // (enabled above via trackCrashes(true)) to actually report anywhere.
    val rumConfiguration = RumConfiguration.Builder(applicationId = BuildKonfig.DATADOG_RUM_APPLICATION_ID)
        .trackLongTasks()
        .apply { configureRumPlatformDefaults(this) }
        .build()

    Rum.enable(rumConfiguration)
}

/** "android" or "ios" — added as a Logger tag so events from both platforms remain distinguishable under the shared [BuildKonfig.DATADOG_SERVICE]. */
internal expect val platformTag: String

/** Platform-specific RUM tracking options (e.g. view/action tracking, ANR/watchdog termination tracking). */
internal expect fun configureRumPlatformDefaults(builder: RumConfiguration.Builder)

private class DatadogAppLogger(private val logger: Logger) : AppLoggerApi {
    override fun debug(message: String, throwable: Throwable?) = logger.debug(message, throwable)
    override fun info(message: String, throwable: Throwable?) = logger.info(message, throwable)
    override fun warn(message: String, throwable: Throwable?) = logger.warn(message, throwable)
    override fun error(message: String, throwable: Throwable?) = logger.error(message, throwable)
}
