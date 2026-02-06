package nl.q42.template.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity

private const val MAX_CHARS_IN_LOG = 1200

class CrashlyticsLogWriter(private val crashReporter: CrashReporter) : LogWriter() {
    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        val tagPart = if (tag.isNotBlank()) "[$tag] " else ""
        val limitedMessage = (tagPart + message).take(MAX_CHARS_IN_LOG)

        if (severity < Severity.Error) {
            val errorMessage = throwable?.let {
                " with error: $throwable: ${throwable.message}".take(MAX_CHARS_IN_LOG)
            } ?: ""
            crashReporter.log((limitedMessage + errorMessage).take(MAX_CHARS_IN_LOG))
        } else {
            crashReporter.log("recordNonFatal with message: $limitedMessage")
            crashReporter.recordNonFatal(
                limitedMessage,
                throwable?.stackTraceSafe()
            )
        }
    }
}

private fun Throwable.stackTraceSafe(): String =
    try {
        stackTraceToString()
    } catch (_: Throwable) {
        toString()
    }