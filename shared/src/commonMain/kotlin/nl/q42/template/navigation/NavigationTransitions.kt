package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene

internal class NavigationTransitions(
    val transitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform,
    val popTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform,
    val predictivePopTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.(swipeEdge: Int) -> ContentTransform,
)

/**
 * Screen transitions that feel native on each platform.
 */
@Composable
internal expect fun rememberNavigationTransitions(): NavigationTransitions
