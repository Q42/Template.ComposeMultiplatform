package nl.q42.template.logging

import nl.q42.template.core.utils.logging.AppLoggerApi
import nl.q42.template.core.utils.logging.setAppLogger

/**
 * Installs [logger] as the app-wide [nl.q42.template.core.utils.logging.AppLogger].
 *
 * Call from Swift after initializing the Datadog iOS SDK natively (see `DatadogBootstrap.swift`)
 * — Kotlin/Native cannot link the Datadog SDK's iOS framework outside of Xcode's own SPM
 * resolution, so the SDK calls and the [AppLoggerApi] implementation both live in Swift.
 */
fun installAppLogger(logger: AppLoggerApi) {
    setAppLogger(logger)
}
