package nl.q42.template.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState

/**
 * Ignores back while a screen is still animating in or out.
 *
 * A back swipe that starts during a push makes NavDisplay seek a new transition while the push
 * is still running. Compose then shows the screens in the wrong places, ignores the finger and
 * jumps to the end. This is still the case in navigation3 1.2.0.
 *
 * The handler is registered after the one of NavDisplay, so it gets back events first. A back
 * swipe that is already in progress keeps going to NavDisplay, so this does not interrupt it.
 */
@Composable
internal fun <T : Any> rememberBlockBackDuringTransitionNavEntryDecorator(): NavEntryDecorator<T> = remember {
    NavEntryDecorator { entry ->
        val transition = LocalNavAnimatedContentScope.current.transition
        NavigationBackHandler(
            state = rememberNavigationEventState(NavigationEventInfo.None),
            isBackEnabled = transition.currentState != transition.targetState,
            onBackCompleted = {},
        )
        entry.Content()
    }
}
