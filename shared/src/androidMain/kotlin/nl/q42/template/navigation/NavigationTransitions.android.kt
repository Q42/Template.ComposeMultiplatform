package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene

private const val TAB_FADE_DURATION_MILLIS = 300

@Composable
internal actual fun rememberNavigationTransitions(): NavigationTransitions = remember {
    NavigationTransitions(
        transitionSpec = {
            if (isTabSwitch()) {
                fadeInOverPreviousTab()
            } else {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            }
        },
        popTransitionSpec = {
            if (isTabSwitch()) fadeOutOverNextTab() else slideBack()
        },
        predictivePopTransitionSpec = {
            if (isTabSwitch()) fadeOutOverNextTab() else slideBack()
        },
    )
}

/**
 * Tabs are not spatially related, so Material fades between navigation bar destinations instead of sliding.
 */
private fun AnimatedContentTransitionScope<Scene<NavKey>>.isTabSwitch(): Boolean =
    initialState.isBottomBarTab && targetState.isBottomBarTab

/*
 * Crossfades tabs by fading only the top screen, while the screen underneath stays fully visible. Screens are opaque,
 * so this never shows the empty background, unlike Material's fade through, which fades out the old screen first.
 * NavDisplay draws the new screen on top for a push, but underneath for a pop.
 */
private fun fadeInOverPreviousTab(): ContentTransform =
    fadeIn(tween(TAB_FADE_DURATION_MILLIS, easing = LinearOutSlowInEasing)) togetherWith
            fadeOut(snap(delayMillis = TAB_FADE_DURATION_MILLIS))

private fun fadeOutOverNextTab(): ContentTransform =
    fadeIn(snap()) togetherWith
            fadeOut(tween(TAB_FADE_DURATION_MILLIS, easing = LinearOutSlowInEasing))

private fun slideBack(): ContentTransform =
    slideInHorizontally { -it } + fadeIn() togetherWith
            slideOutHorizontally { it } + fadeOut()
