package nl.q42.template.core.ui.compose.composables.window

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import nl.q42.template.core.ui.compose.composables.navigation.NativeNavigationController
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.LocalThemeIsDark
import nl.q42.template.core.ui.util.isLiquidGlassAvailable
import nl.q42.template.core.ui.util.toUIColor
import platform.UIKit.UIAction
import platform.UIKit.UIButton
import platform.UIKit.UIButtonConfiguration
import platform.UIKit.UIImage
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.accessibilityLabel
import platform.UIKit.navigationController

@Composable
actual fun AppBackButton(
    onClick: () -> Unit,
    contentDescription: String,
) {
    val navigationController = LocalUIViewController.current.navigationController as? NativeNavigationController
    when {
        navigationController?.hasVerticalBar == true ->
            VerticalBarBackButton(navigationController, onClick, contentDescription)
        isLiquidGlassAvailable -> GlassBackButton(onClick, contentDescription)
        // On iOS < 26 a native glass button is not available
        else -> IconBackButton(onClick, contentDescription)
    }
}

/** Shows the back button in the vertical bar (side rail on iPhone Duo) while composed. Draws nothing itself. */
@Composable
private fun VerticalBarBackButton(
    navigationController: NativeNavigationController,
    onClick: () -> Unit,
    contentDescription: String,
) {
    val currentOnClick by rememberUpdatedState(onClick)
    val owner = remember { Any() }
    val isDark = LocalThemeIsDark.current.value
    DisposableEffect(navigationController, contentDescription, isDark) {
        navigationController.showBackButton(owner, contentDescription, isDark) { currentOnClick() }
        onDispose { navigationController.hideBackButton(owner) }
    }
}

/**
 * Native UIButton with the Liquid Glass look, like the back button of a UINavigationBar.
 *
 * Placed above the Compose canvas (`placedAsOverlay`): below it, the glass would not see the Compose content
 * behind it. The top bar does not scroll, so drawing over clipped content is not an issue here.
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun GlassBackButton(
    onClick: () -> Unit,
    contentDescription: String,
) {
    val currentOnClick by rememberUpdatedState(onClick)
    val isDark = LocalThemeIsDark.current.value
    val tint = AppTheme.colors.textPrimary
    UIKitView(
        factory = {
            val configuration = UIButtonConfiguration.glassButtonConfiguration().apply {
                image = UIImage.systemImageNamed("chevron.backward")
            }
            UIButton.buttonWithConfiguration(
                configuration,
                primaryAction = UIAction.actionWithHandler { currentOnClick() },
            )
        },
        update = { button ->
            button.accessibilityLabel = contentDescription
            button.configuration = button.configuration?.apply { baseForegroundColor = tint.toUIColor() }
            // Follow the app theme rather than the system, in case the app overrides dark mode
            button.overrideUserInterfaceStyle =
                if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        },
        // The top bar already insets its navigation icon a bit, together this matches the leading margin of UINavigationBar
        modifier = Modifier.padding(start = Dimens.spacingSmallMedium).size(Dimens.glassButtonSizeIos),
        properties = UIKitInteropProperties(placedAsOverlay = true),
    )
}
