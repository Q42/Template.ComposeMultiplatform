package nl.q42.template.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity

object LoggerBootstrap {
    fun initialize(
        isDebug: Boolean,
        logWriter: LogWriter,
        crashReporter: CrashReporter,
    ) {
        Logger.setTag("Template")

        if (isDebug) {
            Logger.setMinSeverity(Severity.Verbose)
            Logger.setLogWriters(logWriter)

        } else {
            Logger.setMinSeverity(Severity.Info)
            Logger.setLogWriters(
                logWriter,
                CrashlyticsLogWriter(crashReporter)
            )
        }
    }
}