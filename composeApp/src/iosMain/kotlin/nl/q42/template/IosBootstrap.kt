package nl.q42.template

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import nl.q42.template.logging.CrashlyticsLogWriter
import nl.q42.template.logging.IosConsoleLogWriter

object IOSBootstrap {
    fun initialize(isDebug: Boolean) {
        Logger.setTag("Template")

        if (isDebug) {
            Logger.setMinSeverity(Severity.Verbose)
            Logger.setLogWriters(IosConsoleLogWriter())
        } else {
            Logger.setMinSeverity(Severity.Debug)
            Logger.setLogWriters(
                IosConsoleLogWriter(),
                CrashlyticsLogWriter()
            )
        }
    }
}