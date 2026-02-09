package nl.q42.template.logging

import co.touchlab.crashkios.crashlytics.enableCrashlytics
import co.touchlab.crashkios.crashlytics.setCrashlyticsUnhandledExceptionHook

object IOSCrashlytics {
    fun configure() {
        enableCrashlytics()
        setCrashlyticsUnhandledExceptionHook()
    }
}