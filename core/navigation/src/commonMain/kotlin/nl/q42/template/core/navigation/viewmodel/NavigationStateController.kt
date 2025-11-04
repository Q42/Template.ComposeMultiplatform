package nl.q42.template.core.navigation.viewmodel

import androidx.navigation.NavHostController
import kotlin.uuid.ExperimentalUuidApi

/**
 * Navigates to [navigationState].
 */
@OptIn(ExperimentalUuidApi::class)
fun updateNavigationState(
    navController: NavHostController,
    navigationState: NavigationState,
    onNavigated: (navState: NavigationState) -> Unit,
) {
    when (navigationState) {
        is NavigationState.NavigateToRoute -> {
            when (navigationState.backstackBehavior) {
                BackstackBehavior.Default -> {
                }

                BackstackBehavior.RemoveCurrent -> {
                    navController.popBackStack()
                }

                BackstackBehavior.Clear -> {
                    navController.popBackStack(
                        navController.graph.id,
                        false
                    )
                }
            }
            navController.navigate(navigationState.destination)
            onNavigated(navigationState)
        }

        is NavigationState.PopToDestination -> {
            navController.popBackStack(navigationState.destination, false)
            onNavigated(navigationState)
        }

        is NavigationState.NavigateUp -> {
            navController.navigateUp()
            onNavigated(navigationState)
        }

        is NavigationState.Idle -> {
        }
    }
}