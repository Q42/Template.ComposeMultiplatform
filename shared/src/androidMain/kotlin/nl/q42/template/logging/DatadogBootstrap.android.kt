package nl.q42.template.logging

import com.datadog.kmp.rum.configuration.RumConfiguration
import com.datadog.kmp.rum.configuration.trackNonFatalAnrs

internal actual val platformTag: String = "android"

internal actual fun configureRumPlatformDefaults(builder: RumConfiguration.Builder) {
    builder.trackNonFatalAnrs(true)
    // The app is a single Activity hosting Compose Multiplatform navigation, so Datadog's
    // automatic Activity-based view tracking wouldn't produce meaningful views. Track views
    // manually (RumMonitor.startView/stopView) per navigation destination if you want RUM views.
}
