package nl.q42.template.core.ui.compose.composables.navigation

import org.jetbrains.compose.resources.DrawableResource

/**
 * A single tab in [AppNavigationBar].
 *
 * @param icon used by the Compose (Material) navigation bar.
 * @param iosImageName image in the iOS app asset catalog, used by the native iOS tab bar.
 */
data class AppNavigationBarItem(
    val label: String,
    val icon: DrawableResource,
    val iosImageName: String,
)
