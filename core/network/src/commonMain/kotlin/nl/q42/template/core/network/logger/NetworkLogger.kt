package nl.q42.template.core.network.logger

import io.ktor.client.plugins.logging.Logger

/**
 * Ktor logger that uses Kermit for multiplatform logging
 */
class NetworkLogger : Logger {
    override fun log(message: String) {
        co.touchlab.kermit.Logger.d { message }
    }
}

