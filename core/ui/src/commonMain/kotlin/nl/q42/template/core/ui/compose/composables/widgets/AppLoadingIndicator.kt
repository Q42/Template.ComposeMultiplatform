package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme

/**
 * Indeterminate loading indicator that looks native on each platform:
 * a Material circular spinner on Android and an iOS-style activity indicator (spokes) on iOS.
 *
 * @param size use one of the `Dimens.loadingIndicatorSize*` values.
 */
@Composable
expect fun AppLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = Dimens.loadingIndicatorSizeMedium,
    color: Color = AppTheme.colors.accent,
)

@Preview
@Composable
private fun AppLoadingIndicatorPreview() {
    PreviewAppTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AppLoadingIndicator(size = Dimens.loadingIndicatorSizeSmall)
            AppLoadingIndicator(size = Dimens.loadingIndicatorSizeMedium)
            AppLoadingIndicator(size = Dimens.loadingIndicatorSizeLarge)
        }
    }
}
