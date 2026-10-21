package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * Bottom navigation bar. Only renders the bar, navigation itself is up to the caller.
 *
 * - Android and iOS < 26: Material [MaterialNavigationBar].
 * - iOS 26+: a native UITabBar, so it gets the Liquid Glass look.
 *
 * Callers should place this at the bottom of the screen, on top of the content.
 *
 * @param onHeightChanged reports the height of the bar, including the bottom system insets.
 * Provide it as [LocalAppNavigationBarHeight] to the content behind the bar.
 * @param modifier ignored on iOS 26+: the native tab bar is added to the root view controller and always spans
 * the bottom of the screen, like a UITabBarController's tab bar.
 */
@Composable
expect fun AppNavigationBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
    modifier: Modifier = Modifier,
)
