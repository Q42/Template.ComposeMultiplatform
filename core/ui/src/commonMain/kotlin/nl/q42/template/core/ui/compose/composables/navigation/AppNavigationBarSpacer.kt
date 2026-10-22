package nl.q42.template.core.ui.compose.composables.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Height of the [AppNavigationBar] drawn on top of the screen, including the bottom insets. 0 when there is none. */
val LocalAppNavigationBarHeight = compositionLocalOf { 0.dp }

/**
 * Use as Scaffold `bottomBar`. The Scaffold then adds the height of the [AppNavigationBar] to the content padding
 * and places snackbars and FABs above it, while the content still draws behind the (translucent) bar.
 */
@Composable
fun AppNavigationBarSpacer() {
    val height: Dp = LocalAppNavigationBarHeight.current
    // Without a bottomBar, Scaffold uses the navigation bar insets instead
    if (height > 0.dp) Spacer(Modifier.height(height))
}
