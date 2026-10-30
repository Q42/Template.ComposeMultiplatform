package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.unveilIn
import androidx.compose.animation.veilOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import androidx.navigationevent.NavigationEvent
import nl.q42.template.core.ui.theme.AppTheme

/*
 * Mimics the UINavigationController push/pop: the new screen slides in over the full width, while
 * the screen underneath moves along a bit (parallax) and dims. The timing and easing are taken from
 * the navigation3 iOS defaults, which approximate UIKit's no-bounce spring; the parallax and dim
 * amount are closer to UIKit than those defaults.
 */
private const val TRANSITION_DURATION_MILLIS = 500
private const val PARALLAX_FRACTION = 0.3f
private val TransitionEasing = CubicBezierEasing(0.2833f, 0.99f, 0.31833f, 0.99f)

@Composable
internal actual fun rememberNavigationTransitions(): NavigationTransitions {
    val dimColor = AppTheme.colors.navigationTransitionDim
    return remember(dimColor) {
        NavigationTransitions(
            transitionSpec = {
                if (isTabSwitch()) noTransition() else slide(SlideDirection.Left, isPop = false, dimColor, TransitionEasing)
            },
            popTransitionSpec = {
                if (isTabSwitch()) noTransition() else slide(SlideDirection.Right, isPop = true, dimColor, TransitionEasing)
            },
            // The back swipe drives the transition progress, so it must follow the finger linearly
            predictivePopTransitionSpec = { swipeEdge ->
                val towards = if (swipeEdge == NavigationEvent.EDGE_RIGHT) SlideDirection.Left else SlideDirection.Right
                slide(towards, isPop = true, dimColor, LinearEasing)
            },
        )
    }
}

/**
 * Like UITabBarController, switching tabs swaps the screen without animating.
 */
private fun AnimatedContentTransitionScope<Scene<NavKey>>.isTabSwitch(): Boolean =
    initialState.isBottomBarTab && targetState.isBottomBarTab

private fun noTransition() = ContentTransform(EnterTransition.None, ExitTransition.None)

/**
 * Slides the top screen over the full width, and the screen underneath by [PARALLAX_FRACTION] while dimming it.
 */
@OptIn(ExperimentalAnimationApi::class) // veilOut and unveilIn
private fun AnimatedContentTransitionScope<Scene<NavKey>>.slide(
    towards: SlideDirection,
    isPop: Boolean,
    dimColor: Color,
    easing: Easing,
): ContentTransform {
    val enter = if (isPop) {
        slideIntoContainer(towards, transitionTween(easing), initialOffset = ::parallaxOffset) +
                unveilIn(transitionTween(easing), initialColor = dimColor)
    } else {
        slideIntoContainer(towards, transitionTween(easing))
    }
    val exit = if (isPop) {
        slideOutOfContainer(towards, transitionTween(easing))
    } else {
        slideOutOfContainer(towards, transitionTween(easing), targetOffset = ::parallaxOffset) +
                veilOut(transitionTween(easing), targetColor = dimColor)
    }
    return ContentTransform(enter, exit)
}

private fun parallaxOffset(fullWidth: Int): Int = (fullWidth * PARALLAX_FRACTION).toInt()

private fun <T> transitionTween(easing: Easing): TweenSpec<T> = tween(TRANSITION_DURATION_MILLIS, easing = easing)
