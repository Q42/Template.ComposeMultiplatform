package nl.q42.template.logging

interface CrashReporterDelegate {
    fun log(message: String)
    fun recordNonFatal(message: String, stackTrace: String?)
}