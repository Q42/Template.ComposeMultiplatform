package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.LocalThemeIsDark
import nl.q42.template.core.ui.util.toUIColor
import platform.UIKit.UIUserInterfaceStyle

/**
 * Shows the tab bar of the [NativeTabBarController] around the Compose view controller. UIKit takes care of the
 * Liquid Glass look, placement (on the side on iPhone Duo), accessibility (VoiceOver, Large Content Viewer for large
 * Dynamic Type sizes), safe areas and rotation.
 */
@OptIn(ExperimentalForeignApi::class)
@Composable
internal fun NativeTabBar(
    controller: NativeTabBarController,
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
) {
    val currentOnItemSelected by rememberUpdatedState(onItemSelected)
    val currentOnHeightChanged by rememberUpdatedState(onHeightChanged)

    DisposableEffect(controller) {
        controller.onItemSelected = { currentOnItemSelected(it) }
        controller.onLayout = { currentOnHeightChanged(controller.bottomTabBarHeight().dp) }
        controller.setTabBarHidden(false, animated = true)
        onDispose {
            controller.setTabBarHidden(true, animated = true)
            controller.onItemSelected = {}
            controller.onLayout = {}
            currentOnHeightChanged(0.dp)
        }
    }

    val isDark = LocalThemeIsDark.current.value
    val accent = AppTheme.colors.accent
    SideEffect {
        controller.updateItems(items)
        controller.selectItem(selectedIndex)
        controller.keepContentAboveTabs()
        // Follow the app theme rather than the system, in case the app overrides dark mode
        controller.tabBar.overrideUserInterfaceStyle =
            if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        // On the view, not the tab bar: the side rail on iPhone Duo is not part of the tab bar
        controller.view.tintColor = accent.toUIColor()
    }
}

/** Space the tab bar takes at the bottom of the screen, including the bottom safe area. 0 when it is on the side. */
@OptIn(ExperimentalForeignApi::class)
private fun NativeTabBarController.bottomTabBarHeight(): Double {
    if (tabBar.hidden) return 0.0
    val isVertical = tabBar.frame.useContents { size.height > size.width }
    if (isVertical) return 0.0
    val viewHeight = view.bounds.useContents { size.height }
    val tabBarTop = tabBar.frame.useContents { origin.y }
    return (viewHeight - tabBarTop).coerceIn(0.0, viewHeight)
}
