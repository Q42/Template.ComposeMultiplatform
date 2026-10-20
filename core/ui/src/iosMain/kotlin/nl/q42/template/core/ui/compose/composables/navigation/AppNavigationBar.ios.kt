package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import nl.q42.template.core.ui.util.isLiquidGlassAvailable

/** On iOS < 26 the native UITabBar looks dated, so we use Material there. */
@Composable
actual fun AppNavigationBar(
    items: List<AppNavigationBarItem>,
    selectedIndex: Int,
    onItemSelected: (index: Int) -> Unit,
    onHeightChanged: (Dp) -> Unit,
    modifier: Modifier,
) {
    if (isLiquidGlassAvailable) {
        NativeTabBar(items, selectedIndex, onItemSelected, onHeightChanged, modifier)
    } else {
        MaterialNavigationBar(items, selectedIndex, onItemSelected, onHeightChanged, modifier)
    }
}
