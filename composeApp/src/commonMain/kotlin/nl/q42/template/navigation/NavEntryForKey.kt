package nl.q42.template.navigation

import OnboardingScreen
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.feature.home.ui.HomeScreen
import nl.q42.template.feature.home.ui.InteropExamplesScreen

fun navEntryForKey(key: NavKey, navigator: Navigator) =
    when (key) {
        Destination.Home -> {
            NavEntry(key) {
                HomeScreen(navigator = navigator)
            }
        }
        Destination.Onboarding -> {
            NavEntry(key) {
                OnboardingScreen(navigator = navigator)
            }
        }
        Destination.InteropExamples -> {
            NavEntry(key) {
                InteropExamplesScreen(navigator = navigator)
            }
        }
        else -> error("Unknown NavKey: $key")
    }
