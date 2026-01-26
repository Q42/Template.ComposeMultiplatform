package nl.q42.template.core.network.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import nl.q42.template.core.network.interceptor.ContentTypeInterceptor
import nl.q42.template.core.network.interceptor.UserAgentInterceptor
import nl.q42.template.core.network.logger.NapierLogger
import nl.q42.template.core.utils.config.AppVersionCode
import nl.q42.template.core.utils.config.AppVersionName
import nl.q42.template.core.utils.config.IsLogHttpCalls
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        provideHttpClient(
            engine = get(),
            logHttpCalls = get(),
            appVersionName = get(),
            appVersionCode = get(),
        )
    }

    single<HttpClientEngine> {
        createHttpClientEngine()
    }
}

internal fun provideHttpClient(
    engine: HttpClientEngine,
    logHttpCalls: IsLogHttpCalls,
    appVersionName: AppVersionName,
    appVersionCode: AppVersionCode,
): HttpClient {
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        install(ContentTypeInterceptor)

        install(UserAgentInterceptor) {
            this.appVersionName = appVersionName
            this.appVersionCode = appVersionCode
        }

        if (logHttpCalls.value) {
            install(Logging) {
                logger = NapierLogger()
                level = LogLevel.ALL
            }
        }
    }
}

// Expect functions for platform-specific implementations
expect fun createHttpClientEngine(): HttpClientEngine
expect fun getPlatformInfo(): String

