package nl.q42.template.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import nl.q42.template.core.navigation.viewmodel.RouteNavigator

class OnboardingViewModel(
    private val navigator: RouteNavigator,
) : ViewModel(), RouteNavigator by navigator {

    private val _uiState = MutableStateFlow(OnboardingViewState("Onboarding start"))
    val uiState: StateFlow<OnboardingViewState> = _uiState.asStateFlow()

    fun onBackClicked() {
        navigateUp()
    }
}
