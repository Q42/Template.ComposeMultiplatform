package nl.q42.template.di

import platform.Foundation.NSBundle
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
internal actual fun isDebug(): Boolean = Platform.isDebugBinary

internal actual fun getApplicationId(): String? {
    return NSBundle.mainBundle.bundleIdentifier
}