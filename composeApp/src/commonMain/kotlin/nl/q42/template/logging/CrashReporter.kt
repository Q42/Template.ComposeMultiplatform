package nl.q42.template.logging

interface CrashReporter {
    fun log(message: String)
    fun recordNonFatal(throwable: Throwable)
}