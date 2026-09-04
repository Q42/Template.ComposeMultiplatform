package nl.q42.template.core.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIContentSizeCategory
import platform.UIKit.UIContentSizeCategoryAccessibilityExtraExtraExtraLarge
import platform.UIKit.UIContentSizeCategoryAccessibilityExtraExtraLarge
import platform.UIKit.UIContentSizeCategoryAccessibilityExtraLarge
import platform.UIKit.UIContentSizeCategoryAccessibilityLarge
import platform.UIKit.UIContentSizeCategoryAccessibilityMedium
import platform.UIKit.UIContentSizeCategoryDidChangeNotification
import platform.UIKit.UIContentSizeCategoryExtraExtraExtraLarge
import platform.UIKit.UIContentSizeCategoryExtraExtraLarge
import platform.UIKit.UIContentSizeCategoryExtraLarge
import platform.UIKit.UIContentSizeCategoryExtraSmall
import platform.UIKit.UIContentSizeCategoryLarge
import platform.UIKit.UIContentSizeCategoryMedium
import platform.UIKit.UIContentSizeCategorySmall

/**
 * Compose Multiplatform only reads the iOS "Dynamic Type" text size (`UIContentSizeCategory`)
 * once, when the Compose scene is created. Unlike on Android, changing the system-wide text
 * size while the app is running does not update [LocalDensity.current.fontScale] on iOS, so text
 * only re-scales after the app is restarted
 *
 * This wraps [content] and overrides [LocalDensity] with a font scale that is kept in sync with
 * [UIApplication.preferredContentSizeCategory] by observing [UIContentSizeCategoryDidChangeNotification],
 * so text scales live, matching Android behavior.
 *
 * The issue is closed https://youtrack.jetbrains.com/issue/CMP-10365 but the bug stil exists
 */
@Composable
fun ProvideDynamicTypeDensity(content: @Composable () -> Unit) {
    var fontScale by remember { mutableStateOf(currentFontScale()) }

    DisposableEffect(Unit) {
        val observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIContentSizeCategoryDidChangeNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
        ) {
            fontScale = currentFontScale()
        }
        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(observer)
        }
    }

    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = fontScale),
        content = content,
    )
}

private fun currentFontScale(): Float =
    fontScaleFor(UIApplication.sharedApplication.preferredContentSizeCategory)

/**
 * Mirrors the mapping Compose Multiplatform itself uses when it computes the initial density
 * (see `androidx.compose.ui.uikit.density`), so the live-updated scale matches what the app would
 * show after a restart.
 */
private fun fontScaleFor(category: UIContentSizeCategory): Float = when (category) {
    UIContentSizeCategoryExtraSmall -> 0.8f
    UIContentSizeCategorySmall -> 0.85f
    UIContentSizeCategoryMedium -> 0.9f
    UIContentSizeCategoryLarge -> 1f
    UIContentSizeCategoryExtraLarge -> 1.1f
    UIContentSizeCategoryExtraExtraLarge -> 1.2f
    UIContentSizeCategoryExtraExtraExtraLarge -> 1.3f
    UIContentSizeCategoryAccessibilityMedium -> 1.4f
    UIContentSizeCategoryAccessibilityLarge -> 1.5f
    UIContentSizeCategoryAccessibilityExtraLarge -> 1.6f
    UIContentSizeCategoryAccessibilityExtraExtraLarge -> 1.7f
    UIContentSizeCategoryAccessibilityExtraExtraExtraLarge -> 1.8f
    else -> 1f
}
