package nl.q42.template.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics

class AndroidCrashReporter : CrashReporter {
    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun log(message: String) {
        crashlytics.log(message)
    }

    override fun recordNonFatal(message: String, stackTrace: String?) {
        crashlytics.recordException(
            Exception(
                if (stackTrace != null) {
                    "$message\nStackTrace:\n$stackTrace"
                } else {
                    message
                }
            )
        )
    }
}