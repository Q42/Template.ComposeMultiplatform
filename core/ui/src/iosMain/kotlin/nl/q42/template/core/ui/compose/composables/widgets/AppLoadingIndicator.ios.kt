package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.loading
import org.jetbrains.compose.resources.stringResource

private const val SPOKE_COUNT = 8
private const val REVOLUTION_DURATION_MS = 1000L

// Proportions relative to the indicator size, matched against UIActivityIndicatorView
private const val SPOKE_INNER_RADIUS_FRACTION = 0.2f
private const val SPOKE_OUTER_RADIUS_FRACTION = 0.43f
private const val SPOKE_WIDTH_FRACTION = 0.09f
private const val MIN_SPOKE_ALPHA = 0.25f

/**
 * Compose-drawn look-alike of the native iOS `UIActivityIndicatorView` (8 fading spokes).
 *
 * Why not the native `UIActivityIndicatorView` (via `UIKitView`) or SwiftUI `ProgressView` (via `UIKitViewController`)?
 * Compose Multiplatform places interop views *below* its Metal canvas and cuts a hole in the canvas for them.
 * - **No transparent background**: the hole shows the plain white/black of the Compose container (based on the
 *   system appearance), not the Compose content behind the indicator. This gives a visible box on any colored
 *   surface, and a white box in an app that is dark while the system is light (or vice versa).
 * - Placing the view above the canvas instead (`placedAsOverlay`) makes it transparent, but then it draws over
 *   Compose content and ignores clipping, e.g. spinners in a list scroll over the content above that list.
 * - **Fixed sizes**: the native indicator only comes in 20pt and 37pt, so `size` could not be honored.
 * - **Performance**: any visible interop view makes Compose render synchronized with UIKit, for the whole screen.
 *
 * Like the native indicator, the highlight jumps from spoke to spoke, so it only redraws [SPOKE_COUNT]
 * times per second instead of every frame.
 */
@Composable
actual fun AppLoadingIndicator(
    modifier: Modifier,
    size: Dp,
    color: Color,
) {
    val headSpoke by produceState(initialValue = 0) {
        while (true) {
            delay(REVOLUTION_DURATION_MS / SPOKE_COUNT)
            value = (value + 1) % SPOKE_COUNT
        }
    }

    val description = stringResource(Res.string.loading)
    Canvas(
        modifier = modifier
            .progressSemantics()
            .semantics { contentDescription = description }
            .size(size)
    ) {
        repeat(SPOKE_COUNT) { spoke ->
            val stepsBehindHead = (headSpoke - spoke + SPOKE_COUNT) % SPOKE_COUNT
            drawSpoke(spoke, color.copy(alpha = color.alpha * spokeAlpha(stepsBehindHead)))
        }
    }
}

private fun spokeAlpha(stepsBehindHead: Int): Float =
    1f - (1f - MIN_SPOKE_ALPHA) * stepsBehindHead / (SPOKE_COUNT - 1)

private fun DrawScope.drawSpoke(spoke: Int, color: Color) {
    val diameter = size.minDimension
    rotate(degrees = spoke * 360f / SPOKE_COUNT) {
        drawLine(
            color = color,
            start = center - Offset(0f, diameter * SPOKE_INNER_RADIUS_FRACTION),
            end = center - Offset(0f, diameter * SPOKE_OUTER_RADIUS_FRACTION),
            strokeWidth = diameter * SPOKE_WIDTH_FRACTION,
            cap = StrokeCap.Round,
        )
    }
}
