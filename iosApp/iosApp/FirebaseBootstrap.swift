import ComposeApp
import FirebaseCore
import FirebaseCrashlytics
import Foundation
// Import FirebaseAnalytics and FirebasePerformance here

final class FirebaseBootstrap {
    func configure() {
        let isUIPreview = ProcessInfo.processInfo.environment["XCODE_RUNNING_FOR_PREVIEWS"] == "1"

        #if DEBUG
        let isDebug = true
        #else
        let isDebug = false
        #endif

        // Enable general Firebase in debug mode, but disable Crashlytics, Analytics, and Performance
        // This allows us to use some services (e.g. Remote Config) during development if needed
        let isFirebaseEnabled = !isUIPreview
        let isCrashlyticsEnabled = isFirebaseEnabled && !isDebug
        var isCrashlyticsLoggingEnabled = false

        if isFirebaseEnabled {
            if let firebaseConfigFileName = Bundle.main.object(forInfoDictionaryKey: "FIREBASE_CONFIGURATION_FILE") as? String,
               let firebaseConfigPath = Bundle.main.path(forResource: firebaseConfigFileName.replacingOccurrences(of: ".plist", with: ""), ofType: "plist"),
               let firebaseOptions = FirebaseOptions(contentsOfFile: firebaseConfigPath) {
                FirebaseApp.configure(options: firebaseOptions)

                // Uncomment the following lines if you want to use Analytics and Performance.
                // Analytics.setAnalyticsCollectionEnabled(isAnalyticsEnabled)
                // Performance.sharedInstance().isDataCollectionEnabled = isPerformanceEnabled
                Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(isCrashlyticsEnabled)

                if isCrashlyticsEnabled {
                    IOSCrashlytics.shared.configure()
                    isCrashlyticsLoggingEnabled = true
                }
            } else {
                assertionFailure("Failed to initialise Firebase SDK")
            }
        }

        LoggerBootstrap.shared.initialize(
            isDebug: isDebug,
            logWriter: IOSConsoleLogWriter(),
            crashReporter: isCrashlyticsLoggingEnabled ? IOSCrashReporter() : NoOpCrashReporter()
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
