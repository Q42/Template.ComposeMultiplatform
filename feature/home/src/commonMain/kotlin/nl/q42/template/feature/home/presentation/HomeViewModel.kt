package nl.q42.template.feature.home.presentation

import androidx.lifecycle.ViewModel
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.navigation.viewmodel.RouteNavigator

class HomeViewModel(
    private val navigator: RouteNavigator,
) : ViewModel(), RouteNavigator by navigator {

    fun onOpenSecondScreenClicked() {
        navigateTo(Destination.HomeSecond(title = "Hello world!"))
    }

    fun onOpenOnboardingClicked() {
        navigateTo(Destination.Onboarding)
    }
}
