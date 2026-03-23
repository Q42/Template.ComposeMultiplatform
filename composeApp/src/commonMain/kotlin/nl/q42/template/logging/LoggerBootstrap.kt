package nl.q42.template.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import nl.q42.template.BuildKonfig

object LoggerBootstrap {
    fun initialize(
        logWriter: LogWriter,
        crashReporter: CrashReporter,
    ) {
        Logger.setTag("Template")

        if (BuildKonfig.DEBUG) {
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