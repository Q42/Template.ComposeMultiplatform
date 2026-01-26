package nl.q42.template.core.network.di

import android.content.Context
import android.os.Build
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Cache
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit


actual fun createHttpClientEngine(): HttpClientEngine {
    return OkHttp.create {
        config {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)

            // Add cache through Koin context
            val context = object : KoinComponent {
                val ctx: Context by inject()
            }.ctx
            cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
        }
    }
}

actual fun getPlatformInfo(): String {
    val androidVersionRelease = Build.VERSION.RELEASE
    return "Android/$androidVersionRelease; ${Build.BRAND} ${Build.MODEL}"
}