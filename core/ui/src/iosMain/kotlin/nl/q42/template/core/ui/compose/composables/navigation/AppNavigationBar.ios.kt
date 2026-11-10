package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.unit.Dp

/**
 * On iOS 26+ the Compose view controller is inside a [NativeTabBarController], see [withNativeTabBarController].
 * On iOS < 26 the native UITabBar looks dated, so we use Material there.
 */
@Composable
actual fun AppNavigationBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
) {
    val tabBarController = LocalUIViewController.current.parentViewController as? NativeTabBarController
    if (tabBarController != null) {
        NativeTabBar(tabBarController, items, selectedIndex, onItemSelected, onHeightChanged)
    } else {
        MaterialNavigationBar(items, selectedIndex, onItemSelected, onHeightChanged)
    }
}
