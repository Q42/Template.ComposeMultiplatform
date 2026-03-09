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
            if FirebaseApp.app() == nil {
                FirebaseApp.configure()
            }

            IOSCrashlytics.shared.configure()
            Crashlytics.crashlytics().setCrashlyticsCollectionEnabled(true)
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
