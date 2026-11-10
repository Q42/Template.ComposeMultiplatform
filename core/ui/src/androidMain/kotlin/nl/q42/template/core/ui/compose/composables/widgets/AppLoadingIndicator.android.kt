package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.loading
import nl.q42.template.core.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource

/** Material's default stroke is 4dp at 40dp, keep that ratio for the other sizes. */
private const val STROKE_WIDTH_FRACTION = 0.1f

@Composable
actual fun AppLoadingIndicator(
    modifier: Modifier,
    size: AppLoadingIndicatorSize,
    color: Color,
) {
    val description = stringResource(Res.string.loading)
    val diameter = size.diameter()
    CircularProgressIndicator(
        modifier = modifier
            .semantics { contentDescription = description }
            .size(diameter),
        color = color.takeOrElse { ProgressIndicatorDefaults.circularColor },
        strokeWidth = diameter * STROKE_WIDTH_FRACTION,
    )
}

private fun AppLoadingIndicatorSize.diameter(): Dp = when (this) {
    AppLoadingIndicatorSize.Small -> Dimens.loadingIndicatorSizeSmallAndroid
    AppLoadingIndicatorSize.Medium -> Dimens.loadingIndicatorSizeMediumAndroid
    AppLoadingIndicatorSize.Large -> Dimens.loadingIndicatorSizeLargeAndroid
}
