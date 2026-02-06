package nl.q42.template.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity

class CrashlyticsLogWriter : LogWriter() {
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
            CrashReporter.log((limitedMessage + errorMessage).take(MAX_CHARS_IN_LOG))
        } else {
            CrashReporter.log("recordNonFatal with message: $limitedMessage")
            CrashReporter.recordNonFatal(
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