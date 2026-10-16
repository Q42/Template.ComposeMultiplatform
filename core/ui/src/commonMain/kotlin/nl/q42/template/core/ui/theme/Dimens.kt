package nl.q42.template.core.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp

/**
 * All sizes used in the UI: paddings, spacings, widths and heights. Never hardcode `.dp` values in screens.
 *
 * Start from the spacing scale. Add a semantic name (e.g. [buttonSpacingVertical]) when a value has a
 * specific meaning, preferably based on the spacing scale. Corner radii live in [AppShapes].
 */
object Dimens {
    // Spacing
    val spacingSmall = 8.dp
    val spacingSmallMedium = 12.dp
    val spacingMedium = 16.dp
    val spacingLarge = 24.dp
    val spacingExtraLarge = 32.dp

    /** Spacing between 2 vertically stacked components */
    val componentSpacingVertical = spacingLarge

    /** Spacing between 2 vertically stacked buttons */
    val buttonSpacingVertical = spacingSmall

    // Buttons
    val buttonHeight = 40.dp
    val buttonPadding = PaddingValues(horizontal = spacingLarge, vertical = spacingSmall)

    /** Standard icon size */
    val iconSize = 24.dp

    /** Height of the top app bar / matching placeholder spacer. */
    val topBarHeight = 64.dp

    // Screen
    val screenPaddingVertical = spacingExtraLarge
    val screenPaddingHorizontal = spacingLarge
    val screenContentPadding =
        PaddingValues(
            horizontal = screenPaddingHorizontal,
            vertical = screenPaddingVertical
        )
}
