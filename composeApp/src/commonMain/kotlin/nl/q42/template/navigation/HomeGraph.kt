package nl.q42.template.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import nl.q42.template.core.navigation.Destination
import nl.q42.template.feature.home.presentation.HomeViewModel
import nl.q42.template.feature.home.ui.HomeScreen
import nl.q42.template.feature.home.ui.InteropExamplesScreen
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
) {
    navigation<Destination.HomeGraph>(startDestination = Destination.Home) {

        composable<Destination.Home> {

            val viewModel: HomeViewModel = koinViewModel()
            InitNavigator(navController = navController, routeNavigator = viewModel)

            HomeScreen(viewModel)
        }
        composable<Destination.HomeSecond> {
            // TODO
        }
        composable<Destination.InteropExamples> {
            InteropExamplesScreen()
        }
    }
}