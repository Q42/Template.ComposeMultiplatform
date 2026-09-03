import ComposeApp
import DatadogCore
import DatadogCrashReporting
import DatadogInternal
import DatadogLogs
import DatadogRUM
import Foundation

// Read from Datadog.xcconfig (at the repo root, via Info.plist substitution) — the single source
// of truth shared with the Android build (see shared/build.gradle.kts).
private let datadogClientToken = Bundle.main.object(forInfoDictionaryKey: "DATADOG_CLIENT_TOKEN") as! String
private let datadogRumApplicationId = Bundle.main.object(forInfoDictionaryKey: "DATADOG_RUM_APPLICATION_ID") as! String
private let datadogSiteRawValue = Bundle.main.object(forInfoDictionaryKey: "DATADOG_SITE") as! String
private let datadogService = Bundle.main.object(forInfoDictionaryKey: "DATADOG_SERVICE") as! String

final class DatadogBootstrap {
    func configure() {
        #if DEBUG
        let isDebug = true
        #else
        let isDebug = false
        #endif

        guard let datadogSite = DatadogSite(rawValue: datadogSiteRawValue) else {
            fatalError("Invalid DATADOG_SITE value in Datadog.xcconfig: \(datadogSiteRawValue)")
        }

        Datadog.initialize(
            with: Datadog.Configuration(
                clientToken: datadogClientToken,
                env: isDebug ? "development" : "production",
                site: datadogSite,
                service: datadogService
            ),
            trackingConsent: .granted
        )
        Datadog.verbosityLevel = isDebug ? .debug : nil

        Logs.enable()

        let logger = Logger.create(
            with: Logger.Configuration(
                name: "Template",
                remoteLogThreshold: isDebug ? .debug : .info,
                consoleLogFormat: isDebug ? .short : nil
            )
        )
        logger.addTag(withKey: "platform", value: "ios")

        IOSLoggerBootstrapKt.installAppLogger(logger: DatadogAppLogger(logger: logger))

        // Crash reports are delivered as RUM errors, so RUM must be enabled first.
        RUM.enable(with: RUM.Configuration(applicationID: datadogRumApplicationId))
        CrashReporting.enable()
    }
}
