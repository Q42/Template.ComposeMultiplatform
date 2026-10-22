package nl.q42.template.core.ui.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo

/** Liquid Glass was introduced in iOS 26. Native UIKit components get it automatically from that version on. */
val isLiquidGlassAvailable: Boolean by lazy { isIosAtLeast(26) }

@OptIn(ExperimentalForeignApi::class)
fun isIosAtLeast(majorVersion: Int, minorVersion: Int = 0): Boolean =
    NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
        cValue<NSOperatingSystemVersion> {
            this.majorVersion = majorVersion.toLong()
            this.minorVersion = minorVersion.toLong()
            patchVersion = 0
        }
    )
