package nl.q42.template.core.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import platform.UIKit.UIDevice

actual fun createHttpClientEngine(): HttpClientEngine {
    return Darwin.create {
        configureRequest {
            setTimeoutInterval(60.0)
        }
    }
}

actual fun getPlatformInfo(): String {
    val device = UIDevice.currentDevice
    val systemVersion = device.systemVersion
    val model = device.model
    return "iOS/$systemVersion; $model"
}