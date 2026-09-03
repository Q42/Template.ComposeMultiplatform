package nl.q42.template.logging

import com.datadog.kmp.rum.configuration.RumConfiguration
import com.datadog.kmp.rum.configuration.trackWatchdogTerminations

internal actual val platformTag: String = "ios"

internal actual fun configureRumPlatformDefaults(builder: RumConfiguration.Builder) {
    builder.trackWatchdogTerminations(true)
    // The app is a single ComposeUIViewController hosting Compose Multiplatform navigation, so
    // Datadog's automatic UIKit/SwiftUI view tracking (trackUiKitViews/trackSwiftUIViews)
    // wouldn't produce meaningful views. Track views manually per navigation destination instead
    // if you want RUM views.
}
