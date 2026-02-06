package nl.q42.template.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSLog
import platform.Foundation.NSString
import platform.Foundation.create

class IosConsoleLogWriter : LogWriter() {
    @OptIn(BetaInteropApi::class)
    override fun log(
        severity: Severity,
        message: String,
        tag: String,
        throwable: Throwable?
    ) {
        val tagPart = if (tag.isNotBlank()) "[$tag] " else ""
        val base = tagPart + message
        val full = throwable?.let { "$base | ${it::class.simpleName}: ${it.message}" } ?: base
        val nsFull = NSString.create(string = full)
        NSLog("%@", nsFull)
    }
}