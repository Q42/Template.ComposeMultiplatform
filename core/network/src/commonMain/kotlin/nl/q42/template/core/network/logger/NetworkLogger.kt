package nl.q42.template.core.network.logger

import io.ktor.client.plugins.logging.Logger
import nl.q42.template.core.utils.logging.AppLogger

/**
 * Ktor logger that forwards to the app-wide Datadog logger for multiplatform logging
 */
class NetworkLogger : Logger {
    override fun log(message: String) {
        AppLogger.debug(message)
    }
}
