import ComposeApp
import FirebaseCore
import FirebaseCrashlytics
import Foundation

final class FirebaseBootstrap {
    func configure() {
        let isUIPreview = ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"

        #if DEBUG
        let isDebug = true
        #else
        let isDebug = false
        #endif

        let isFirebaseEnabled = !isUIPreview && !isDebug

        if isFirebaseEnabled {
            if let firebaseConfigFileName = Bundle.main.object(forInfoDictionaryKey: "FIREBASE_CONFIGURATION_FILE") as? String,
               let firebaseConfigPath = Bundle.main.path(forResource: firebaseConfigFileName.replacingOccurrences(of: ".plist", with: ""), ofType: "plist"),
               let firebaseOptions = FirebaseOptions(contentsOfFile: firebaseConfigPath) {
                FirebaseApp.configure(options: firebaseOptions)
                IOSCrashlytics.shared.configure()
                Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
            } else {
                assertionFailure("Failed to initialise Firebase SDK")
            }
        }

        LoggerBootstrap.shared.initialize(
            logWriter: IOSConsoleLogWriter(),
            crashReporter: isFirebaseEnabled ? IOSCrashReporter() : NoOpCrashReporter()
        )
    }

    final class NoOpCrashReporter: NSObject, CrashReporter {
        func log(message: String) { /* no-op */ }
        func recordNonFatal(throwable: KotlinThrowable) { /* no-op */ }
    }

    final class IOSCrashReporter: NSObject, CrashReporter {
        func log(message: String) {
            Crashlytics.crashlytics().log(message)
        }

        func recordNonFatal(throwable: KotlinThrowable) {
            CrashlyticsKotlin.shared.sendHandledException(throwable: throwable)
        }
    }
}
