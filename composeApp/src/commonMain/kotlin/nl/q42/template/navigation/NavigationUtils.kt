package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Returns true if this back stack entry is the root of a modal navigation graph.
 *
 * TODO: currently this assumes that any nested navigation graph is a modal graph.
 */
internal fun NavBackStackEntry?.isModalRoot(): Boolean {
    return this?.destination?.let {
        destination ->
            destination.parent?.let { graph ->
                graph.parent != null && destination.route == graph.startDestinationRoute
            } ?: false
        } ?: false
}

/**
 * A composable that has modal-aware enter and exit transitions.
 *
 * When navigating away from a modal root, the exit transition is a fade out.
 * When navigating back to a modal root, the enter transition is a fade in.
 * For all other cases, horizontal slide transitions are used.
 */
internal inline fun <reified T : Any> NavGraphBuilder.modalEnabledComposable(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = {
            if (targetState.isModalRoot()) {
                slideInVertically { it }
            } else {
                slideInHorizontally { it }
            }
        },
        exitTransition = {
            if (targetState.isModalRoot()) {
                ExitTransition.None
            } else {
                slideOutHorizontally()
            }
        },
        popEnterTransition = {
            if (initialState.isModalRoot()) {
                EnterTransition.None
            } else {
                slideInHorizontally { -it } // No enter transition on the background screen when modal is dismissed or a screen inside a modal is popped with the whole modal flow
            }
        },
        popExitTransition = {
            if (initialState.isModalRoot()) {
                slideOutVertically { -it }
            } else {
                slideOutHorizontally { it }
            }
        },
        content = content
    )
}

