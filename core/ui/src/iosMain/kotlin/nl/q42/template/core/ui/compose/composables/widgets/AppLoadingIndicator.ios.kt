package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.delay
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.loading
import nl.q42.template.core.ui.theme.Dimens
import org.jetbrains.compose.resources.stringResource
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIColor
import platform.UIKit.UITraitCollection
import platform.UIKit.UIUserInterfaceStyle
import platform.UIKit.resolvedColorWithTraitCollection

private const val SPOKE_COUNT = 8
private const val REVOLUTION_DURATION_MS = 1000L

// Proportions relative to the indicator size, measured from UIActivityIndicatorView (iOS 26).
// The radii are the ends of the spoke's center line; the round caps extend the spoke by half its width,
// so it covers 0.147 to 0.477 of the size.
private const val SPOKE_INNER_RADIUS_FRACTION = 0.215f
private const val SPOKE_OUTER_RADIUS_FRACTION = 0.41f
private const val SPOKE_WIDTH_FRACTION = 0.135f

// Opacity of a spoke relative to the head spoke, by how many steps it trails the head, as in
// UIActivityIndicatorView: the 3 spokes behind the head fade out evenly, the others stay at the minimum.
// Measured on the iOS 26 simulator; equal to Flutter's CupertinoActivityIndicator (147, 122, 97, 72, 47 / 147).
private val SPOKE_ALPHAS = floatArrayOf(1f, 0.83f, 0.66f, 0.49f, 0.32f)

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
 * - **Performance**: any visible interop view makes Compose render synchronized with UIKit, for the whole screen.
 *
 * Like the native indicator, the highlight jumps from spoke to spoke, so it only redraws [SPOKE_COUNT]
 * times per second instead of every frame.
 */
@Composable
actual fun AppLoadingIndicator(
    modifier: Modifier,
    size: AppLoadingIndicatorSize,
    color: Color,
) {
    val spokeColor = color.takeOrElse { nativeIndicatorColor() }
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
            .size(size.diameter())
    ) {
        repeat(SPOKE_COUNT) { spoke ->
            val stepsBehindHead = (headSpoke - spoke + SPOKE_COUNT) % SPOKE_COUNT
            drawSpoke(spoke, spokeColor.copy(alpha = spokeColor.alpha * spokeAlpha(stepsBehindHead)))
        }
    }
}

private fun AppLoadingIndicatorSize.diameter(): Dp = when (this) {
    AppLoadingIndicatorSize.Small, AppLoadingIndicatorSize.Medium -> Dimens.loadingIndicatorSizeMediumIos
    AppLoadingIndicatorSize.Large -> Dimens.loadingIndicatorSizeLargeIos
}

/** The exact default color of `UIActivityIndicatorView`, a system gray that differs between light and dark mode. */
@Composable
private fun nativeIndicatorColor(): Color {
    val isDark = isSystemInDarkTheme()
    return remember(isDark) {
        val style = if (isDark) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        UIActivityIndicatorView().color
            .resolvedColorWithTraitCollection(UITraitCollection.traitCollectionWithUserInterfaceStyle(style))
            .toComposeColor()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIColor.toComposeColor(): Color = memScoped {
    val red = alloc<DoubleVar>()
    val green = alloc<DoubleVar>()
    val blue = alloc<DoubleVar>()
    val alpha = alloc<DoubleVar>()
    getRed(red.ptr, green.ptr, blue.ptr, alpha.ptr)
    Color(red.value.toFloat(), green.value.toFloat(), blue.value.toFloat(), alpha.value.toFloat())
}

private fun spokeAlpha(stepsBehindHead: Int): Float =
    SPOKE_ALPHAS[minOf(stepsBehindHead, SPOKE_ALPHAS.lastIndex)]

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
