package nl.q42.template.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import nl.q42.template.core.navigation.viewmodel.RouteNavigator
import nl.q42.template.core.navigation.viewmodel.updateNavigationState
import kotlin.uuid.ExperimentalUuidApi

/**
 * Ensures that [routeNavigator] can navigate on this composition. [routeNavigator] will usually be a ViewModel.
 *
 * More info: https://medium.com/@ffvanderlaan/navigation-in-jetpack-compose-using-viewmodel-state-3b2517c24dde
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun InitNavigator(navController: NavHostController, routeNavigator: RouteNavigator) {

    val viewState by routeNavigator.navigationState.collectAsStateWithLifecycle()
    LaunchedEffect(viewState) {
        updateNavigationState(navController, viewState, routeNavigator::onNavigated)
    }
}