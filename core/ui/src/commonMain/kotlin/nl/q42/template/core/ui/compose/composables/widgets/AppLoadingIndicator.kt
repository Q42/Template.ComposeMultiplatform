package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme

/**
 * Indeterminate loading indicator that looks native on each platform:
 * a Material circular spinner on Android and an iOS-style activity indicator (spokes) on iOS.
 */
@Composable
expect fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    size: AppLoadingIndicatorSize = AppLoadingIndicatorSize.Medium,
    color: Color = AppTheme.colors.accent,
)

/**
 * Follows each platform's native sizes, so the actual size differs per platform (see [Dimens]):
 * - Android: 24, 40 and 64dp.
 * - iOS: `UIActivityIndicatorView` only has 2 sizes, so [Small] and [Medium] are both its medium style (20pt)
 *   and [Large] is its large style (37pt).
 */
enum class AppLoadingIndicatorSize { Small, Medium, Large }

@Preview
@Composable
private fun AppLoadingIndicatorPreview() {
    PreviewAppTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppLoadingIndicatorSize.entries.forEach { size ->
                AppLoadingIndicator(size = size)
            }
        }
    }
}
