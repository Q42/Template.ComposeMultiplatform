package nl.q42.template.navigation

import nl.q42.template.feature.sampleonboarding.SampleOnboardingScreen
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.feature.samplehome.ui.SampleHomeScreen
import nl.q42.template.feature.samplehome.ui.SampleInteropScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

internal fun navEntryForKey(key: NavKey, navigator: Navigator): NavEntry<NavKey> =
    when (key) {
        is Destination -> when(key) {
            Destination.Home -> {
                NavEntry(key) {
                    SampleHomeScreen(
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
                    SampleOnboardingScreen(
                        viewModel = koinViewModel(
                            parameters = {
                                parametersOf(navigator)
                            }
                        )
                    )
                }
            }
            Destination.SampleInterop -> {
                NavEntry(key) {
                    SampleInteropScreen(
                        viewModel = koinViewModel(
                            parameters = {
                                parametersOf(navigator)
                            }
                        )
                    )
                }
            }
        }
        else -> error("Unknown NavKey: $key")
    }
