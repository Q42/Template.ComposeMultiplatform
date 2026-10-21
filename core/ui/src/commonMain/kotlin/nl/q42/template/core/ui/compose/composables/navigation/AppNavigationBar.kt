package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/**
 * Bottom navigation bar. Only renders the bar, navigation itself is up to the caller.
 *
 * - Android and iOS < 26: Material [MaterialNavigationBar].
 * - iOS 26+: a native UITabBar, so it gets the Liquid Glass look.
 *
 * Place this on top of the content, filling the screen: the bar is always drawn at the bottom.
 *
 * @param onHeightChanged reports the height of the bar, including the bottom system insets.
 * Provide it as [LocalAppNavigationBarHeight] to the content behind the bar.
 */
@Composable
expect fun AppNavigationBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
)
