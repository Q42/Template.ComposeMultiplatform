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
import nl.q42.template.core.network.interceptor.createUserAgentInterceptor
import nl.q42.template.core.network.logger.NetworkLogger
import nl.q42.template.core.network.model.PlatFormInfo
import nl.q42.template.core.utils.config.AppVersionCode
import nl.q42.template.core.utils.config.AppVersionName
import nl.q42.template.core.utils.config.IsLogHttpCalls
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create

// Centralized cache size configuration for all platforms
internal const val CACHE_SIZE_MB = 10L
internal const val CACHE_SIZE_BYTES = CACHE_SIZE_MB * 1024 * 1024

val networkModule = module {
    single {
        create(::provideHttpClient)
    }

    single<PlatFormInfo> {
        getPlatformInfo()
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
    platFormInfo: PlatFormInfo,
): HttpClient {
    return HttpClient(engine) {

        expectSuccess = true // trow exceptions for non-2xx responses, usually as ClientRequestException

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

        install(createUserAgentInterceptor(platFormInfo)) {
            this.appVersionName = appVersionName
            this.appVersionCode = appVersionCode
        }

        if (logHttpCalls.value) {
            install(Logging) {
                logger = NetworkLogger()
                level = LogLevel.ALL
            }
        }
    }
}

// Expect functions for platform-specific implementations
expect fun createHttpClientEngine(): HttpClientEngine
expect fun getPlatformInfo(): PlatFormInfo

