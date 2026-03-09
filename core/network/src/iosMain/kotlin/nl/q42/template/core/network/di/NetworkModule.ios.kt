package nl.q42.template.core.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import nl.q42.template.core.network.model.PlatFormInfo
import platform.Foundation.NSURLCache
import platform.Foundation.NSURLRequestUseProtocolCachePolicy
import platform.UIKit.UIDevice

actual fun createHttpClientEngine(): HttpClientEngine {
    return Darwin.create {
        configureRequest {
            setTimeoutInterval(60.0)
            setCachePolicy(NSURLRequestUseProtocolCachePolicy)
        }

        configureSession {
            // Configure URL cache with 10 MB memory and disk cache
            val cache = NSURLCache(
                memoryCapacity = CACHE_SIZE_BYTES.toULong(),
                diskCapacity = CACHE_SIZE_BYTES.toULong(),
                diskPath = "http_cache"
            )
            setURLCache(cache)
        }
    }
}

actual fun getPlatformInfo(): PlatFormInfo {
    val device = UIDevice.currentDevice
    val systemVersion = device.systemVersion
    val model = device.model
    return PlatFormInfo("iOS/$systemVersion; $model")
}