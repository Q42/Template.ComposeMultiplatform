import ComposeApp
import FirebaseCore
import FirebaseCrashlytics
import Foundation

final class FirebaseBootstrap {
    func configure() {
        let isUIPreview = ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"
        let isDebug = _isDebugAssertConfiguration()
        let crashlyticsEnabled = !isUIPreview && !isDebug

        if !isUIPreview {
            if FirebaseApp.app() == nil {
                FirebaseApp.configure()
            }

            Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(crashlyticsEnabled)
        }

        LoggerBootstrap.shared.initialize(
            logWriter: IOSConsoleLogWriter(),
            crashReporter: isUIPreview ? NoOpCrashReporter() : IOSCrashReporter()
        )
    }
}

final class NoOpCrashReporter: NSObject, CrashReporter {
    func log(message: String) { /* no-op */ }
    func recordNonFatal(message: String, stackTrace: String?) { /* no-op */ }
}

final class IOSCrashReporter: NSObject, CrashReporter {
    func log(message: String) {
        Crashlytics.crashlytics().log(message)
    }

    func recordNonFatal(throwable: KotlinThrowable) {
        let crashlytics = Crashlytics.crashlytics()
        crashlytics.setCustomValue(throwable.stackTrace.joined(separator: "\n"), forKey: "kotlin_stacktrace")

        let error = NSError(
            domain: String(describing: type(of: throwable)),
            code: 0,
            userInfo: [NSLocalizedDescriptionKey: throwable.message ?? "No message"]
        )
        crashlytics.record(error: error)
    }
}