package nl.q42.template.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import nl.q42.template.core.navigation.Destination
import nl.q42.template.feature.home.presentation.HomeModalExampleViewModel
import nl.q42.template.feature.home.presentation.HomeViewModel
import nl.q42.template.feature.home.presentation.InteropExamplesViewModel
import nl.q42.template.feature.home.ui.HomeModalExampleScreen
import nl.q42.template.feature.home.ui.HomeScreen
import nl.q42.template.feature.home.ui.InteropExamplesScreen
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
) {
    navigation<Destination.HomeGraph>(startDestination = Destination.Home) {

        modalEnabledComposable<Destination.Home> {
            val viewModel: HomeViewModel = koinViewModel()
//            InitNavigator(navController = navController, routeNavigator = viewModel)

            HomeScreen(viewModel)
        }

        modalEnabledComposable<Destination.InteropExamples> {
            val viewModel: InteropExamplesViewModel = koinViewModel()
//            InitNavigator(navController = navController, routeNavigator = viewModel)

//            InteropExamplesScreen(viewModel)
        }

        navigation<Destination.HomeModalExampleGraph>(startDestination = Destination.HomeModalExample) {
            composable<Destination.HomeModalExample>(
                enterTransition = {
                    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Up)
                },
                exitTransition = {
                    slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
                },
                popEnterTransition = {
                    slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right)
                },
                popExitTransition = {
                    slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Down)
                }
            ) {
                val viewModel: HomeModalExampleViewModel = koinViewModel()
                InitNavigator(navController = navController, routeNavigator = viewModel)
                HomeModalExampleScreen(viewModel = viewModel)
            }
        }

    }
}