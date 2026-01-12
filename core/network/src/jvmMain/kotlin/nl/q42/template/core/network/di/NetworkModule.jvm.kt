package nl.q42.template.core.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit

actual fun createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create {
        config {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
        }
    }
}

actual fun getPlatformInfo(): String {
    val osName = System.getProperty("os.name") ?: "Unknown"
    val osVersion = System.getProperty("os.version") ?: "Unknown"
    return "JVM/$osName $osVersion"
}
