package nl.q42.template.logging

import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
import com.datadog.kmp.Datadog
import com.datadog.kmp.DatadogSite
import com.datadog.kmp.SdkLogVerbosity
import com.datadog.kmp.core.configuration.Configuration
import com.datadog.kmp.log.LogLevel
import com.datadog.kmp.log.Logger
import com.datadog.kmp.log.Logs
import com.datadog.kmp.log.configuration.LogsConfiguration
import com.datadog.kmp.privacy.TrackingConsent
import nl.q42.template.BuildKonfig
import nl.q42.template.core.utils.logging.setAppLogger

/**
 * Android-only: initializes the Datadog SDK and installs the app-wide [nl.q42.template.core.utils.logging.AppLogger].
 * The iOS equivalent lives in Swift (`DatadogBootstrap.swift`) since Kotlin/Native cannot link
 * the Datadog SDK's iOS framework outside of Xcode's own SPM resolution.
 */
object AndroidLoggerBootstrap {
    fun initialize(context: Any?, isDebug: Boolean) {
        val configuration = Configuration.Builder(
            clientToken = BuildKonfig.DATADOG_CLIENT_TOKEN,
            env = if (isDebug) "development" else "production",
            service = BuildKonfig.DATADOG_SERVICE,
        )
            .useSite(DatadogSite.valueOf(BuildKonfig.DATADOG_SITE.uppercase()))
            .build()

        Datadog.initialize(context, configuration, TrackingConsent.GRANTED)
        Datadog.verbosity = if (isDebug) SdkLogVerbosity.DEBUG else null

        Logs.enable(LogsConfiguration.Builder().build())

        val logger = Logger.Builder()
            .setName("Template")
            .setPrintLogsToConsole(isDebug)
            .setRemoteLogThreshold(if (isDebug) LogLevel.DEBUG else LogLevel.INFO)
            .build()
        logger.addTag("platform", "android")

        setAppLogger(DatadogAppLogger(logger))

        // Crash reports are delivered as RUM errors, so RUM must be enabled for crash reporting
        // (enabled by default in Configuration.Builder) to actually report anywhere.
        Rum.enable(RumConfiguration.Builder(applicationId = BuildKonfig.DATADOG_RUM_APPLICATION_ID).build())
    }
}
