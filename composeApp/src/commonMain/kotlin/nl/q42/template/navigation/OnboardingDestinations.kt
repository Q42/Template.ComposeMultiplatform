package nl.q42.template.navigation

import OnboardingScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import nl.q42.template.core.navigation.Destination
import nl.q42.template.feature.onboarding.presentation.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel

internal fun NavGraphBuilder.onboardingDestinations(navController: NavHostController) {
    modalEnabledComposable<Destination.Onboarding> {

        val viewModel: OnboardingViewModel = koinViewModel()
        InitNavigator(navController = navController, viewModel)

//        OnboardingScreen(viewModel = viewModel)
    }
}
