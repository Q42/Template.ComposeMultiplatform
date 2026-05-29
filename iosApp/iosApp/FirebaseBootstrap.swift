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

        let isFirebaseEnabled = !isUIPreview
        let isCrashlyticsEnabled = isFirebaseEnabled && !isDebug

        if isFirebaseEnabled {
            guard
                let firebaseConfigFileName = Bundle.main.object(forInfoDictionaryKey: "FIREBASE_CONFIGURATION_FILE") as? String,
                let firebaseConfigPath = Bundle.main.path(forResource: firebaseConfigFileName, ofType: "plist"),
                let firebaseOptions = FirebaseOptions(contentsOfFile: firebaseConfigPath)
            else {
                assertionFailure("Failed to load Firebase SDK")
                return
            }

            FirebaseApp.configure(options: firebaseOptions)

            // Uncomment the following lines if you want to use Analytics and Performance
            // Analytics.setAnalyticsCollectionEnabled(isAnalyticsEnabled)
            // Performance.sharedInstance().isDataCollectionEnabled = isPerformanceEnabled

            Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(isCrashlyticsEnabled)

            if isCrashlyticsEnabled {
                IOSCrashlytics.shared.configure()
            }
        }

        LoggerBootstrap.shared.initialize(
            isDebug: isDebug,
            logWriter: IOSConsoleLogWriter(),
            crashReporter: isCrashlyticsEnabled ? IOSCrashReporter() : NoOpCrashReporter()
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
