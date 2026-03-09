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
        val fullMessage = tagPart + message
        val limitedMessage = fullMessage.take(MAX_CHARS_IN_LOG)

        if (severity < Severity.Error) {
            val errorSuffix = throwable?.let { ": $it" } ?: ""
            crashReporter.log((limitedMessage + errorSuffix).take(MAX_CHARS_IN_LOG))
        } else {
            crashReporter.log("Error event: $limitedMessage")

            val exceptionToRecord = throwable ?: RuntimeException(limitedMessage)
            crashReporter.recordNonFatal(exceptionToRecord)
        }
    }
}