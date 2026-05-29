package nl.q42.template.di

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual fun isDebug(): Boolean = Platform.isDebugBinary

actual fun getAppVersionName(): String? {
    return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String
}

actual fun getAppVersionCode(): Long {
    val build = NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion") as? String
    return build?.toLongOrNull() ?: 0L
}

actual fun getApplicationId(): String? {
    return NSBundle.mainBundle.bundleIdentifier
}