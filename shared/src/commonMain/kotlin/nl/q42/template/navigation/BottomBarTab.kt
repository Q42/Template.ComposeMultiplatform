package nl.q42.template.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.BackstackBehavior
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.core.ui.compose.composables.navigation.AppNavigationBarItem
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.sample_extension_24
import nl.q42.template.core.ui.resources.sample_home_24
import nl.q42.template.core.ui.resources.navigation_tab_home
import nl.q42.template.core.ui.resources.navigation_tab_interop
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Top-level destinations shown in the bottom bar. The bottom bar is visible when one of these is on top of the back stack.
 *
 * All tabs share the single app back stack: the first tab is the root, other tabs are placed on top of it.
 * So back from any tab returns to the first tab, and deep links / state restoration work as before.
 */
internal enum class BottomBarTab(
    val destination: Destination,
    val label: StringResource,
    val icon: DrawableResource,
    val iosImageName: String,
) {
    Home(Destination.Home, Res.string.navigation_tab_home, Res.drawable.sample_home_24, "sample_tab_home"),
    Interop(Destination.SampleInterop, Res.string.navigation_tab_interop, Res.drawable.sample_extension_24, "sample_tab_interop"),
}

private const val BOTTOM_BAR_TAB_METADATA_KEY = "bottomBarTab"

/**
 * Marks the nav entry of a tab, so transitions can tell a tab switch apart from a push or pop.
 */
internal fun bottomBarTabMetadata(key: NavKey): Map<String, Any> =
    if (BottomBarTab.entries.any { it.destination == key }) mapOf(BOTTOM_BAR_TAB_METADATA_KEY to true) else emptyMap()

internal val Scene<NavKey>.isBottomBarTab: Boolean
    get() = metadata[BOTTOM_BAR_TAB_METADATA_KEY] == true

@Composable
internal fun BottomBarTab.toNavigationBarItem() = AppNavigationBarItem(
    label = stringResource(label),
    icon = icon,
    iosImageName = iosImageName,
)

internal fun Navigator.navigateToTab(tab: BottomBarTab) {
    val rootTab = BottomBarTab.entries.first()
    navigateTo(rootTab.destination, BackstackBehavior.Clear)
    if (tab != rootTab) {
        navigateTo(tab.destination)
    }
}
