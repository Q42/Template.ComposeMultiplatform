import ComposeApp
import FirebaseCore
import FirebaseCrashlytics
import Foundation

final class FirebaseBootstrap {
    func configure() {
        let isUIPreview =
            ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"

        #if DEBUG
        let isDebug = true
        #else
        let isDebug = false
        #endif

        let crashlyticsEnabled = !isUIPreview && !isDebug

        FirebaseApp.configure()
        Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(crashlyticsEnabled)

        if crashlyticsEnabled {
            CrashReporter.shared.setDelegate(
                delegate: IOSCrashReporterDelegate()
            )
        }

        IOSBootstrap.shared.initialize(isDebug: isDebug)
    }
}

final class IOSCrashReporterDelegate: NSObject, CrashReporterDelegate {
    func log(message: String) {
        Crashlytics.crashlytics().log(message)
    }

    func recordNonFatal(message: String, stackTrace: String?) {
        let c = Crashlytics.crashlytics()
        if let stackTrace {
            c.setCustomValue(stackTrace, forKey: "kotlin_stacktrace")
        }
        let error = NSError(
            domain: "KotlinNonFatal",
            code: 0,
            userInfo: [NSLocalizedDescriptionKey: message]
        )
        c.record(error: error)
    }
}
