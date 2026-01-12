package nl.q42.template.core.network.logger

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.logging.Logger

/**
 * Ktor logger that uses Napier for multiplatform logging
 */
class NapierLogger : Logger {
    override fun log(message: String) {
        Napier.d { message }
    }
}

