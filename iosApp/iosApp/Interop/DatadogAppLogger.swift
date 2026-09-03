import ComposeApp
import DatadogLogs
import Foundation

/// Bridges the native Datadog iOS `LoggerProtocol` into Kotlin's `UtilsAppLoggerApi`, so shared
/// Kotlin code can log through the app-wide `AppLogger` without Kotlin/Native ever needing to link
/// the Datadog SDK's iOS framework itself (see `installAppLogger` in `IOSLoggerBootstrap.kt`).
final class DatadogAppLogger: NSObject, UtilsAppLoggerApi {
    private let logger: LoggerProtocol

    init(logger: LoggerProtocol) {
        self.logger = logger
    }

    func debug(message: String, throwable: KotlinThrowable?) {
        logger.debug(message, error: throwable?.asError(), attributes: nil)
    }

    func info(message: String, throwable: KotlinThrowable?) {
        logger.info(message, error: throwable?.asError(), attributes: nil)
    }

    func warn(message: String, throwable: KotlinThrowable?) {
        logger.warn(message, error: throwable?.asError(), attributes: nil)
    }

    func error(message: String, throwable: KotlinThrowable?) {
        logger.error(message, error: throwable?.asError(), attributes: nil)
    }
}
