package nl.q42.template.core.ui.compose.composables.window

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.navigation_back_24
import nl.q42.template.core.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

/**
 * Back button for the navigation icon slot of [TopAppBar].
 *
 * - Android: Material icon button with an arrow.
 * - iOS < 26: Compose icon button with a chevron.
 * - iOS 26+: native Liquid Glass button with a chevron.
 * - iOS with a vertical bar (side rail on iPhone Duo): the back button moves into the vertical bar,
 *   so nothing is drawn here.
 */
@Composable
expect fun AppBackButton(
    onClick: () -> Unit,
    contentDescription: String,
)

/** Compose-drawn back button, with the platform icon from [navigation_back_24]. */
@Composable
internal fun IconBackButton(
    onClick: () -> Unit,
    contentDescription: String,
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(Res.drawable.navigation_back_24),
            tint = AppTheme.colors.textPrimary,
            contentDescription = contentDescription,
        )
    }
}
