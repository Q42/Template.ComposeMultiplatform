package nl.q42.template.navigation

import nl.q42.template.feature.onboarding.OnboardingScreen
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.feature.home.ui.HomeScreen
import nl.q42.template.feature.home.ui.InteropExamplesScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun navEntryForKey(key: NavKey, navigator: Navigator) =
    when (key) {
        Destination.Home -> {
            NavEntry(key) {
                HomeScreen(
                    viewModel = koinViewModel(
                        parameters = {
                            parametersOf(navigator)
                        }
                    )
                )
            }
        }
        Destination.Onboarding -> {
            NavEntry(key) {
                OnboardingScreen(
                    viewModel = koinViewModel(
                        parameters = {
                            parametersOf(navigator)
                        }
                    )
                )
            }
        }
        Destination.InteropExamples -> {
            NavEntry(key) {
                InteropExamplesScreen(
                    viewModel = koinViewModel(
                        parameters = {
                            parametersOf(navigator)
                        }
                    )
                )
            }
        }
        else -> error("Unknown NavKey: $key")
    }
