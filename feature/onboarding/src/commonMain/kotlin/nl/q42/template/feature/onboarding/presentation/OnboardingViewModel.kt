package nl.q42.template.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nl.q42.template.core.navigation.viewmodel.RouteNavigator
import nl.q42.template.domain.main.repo.AppSettingsRepository

class OnboardingViewModel(
    private val navigator: RouteNavigator,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel(), RouteNavigator by navigator {

    private val _uiState = MutableStateFlow(OnboardingViewState("Onboarding start"))
    val uiState: StateFlow<OnboardingViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (appSettingsRepository.isOnboardingCompleted()) {
                _uiState.value = OnboardingViewState("Onboarding already completed")
            }
        }
    }

    fun onCompleteOnboardingClicked() {
        viewModelScope.launch {
            appSettingsRepository.setOnboardingCompleted()
            navigateUp()
        }
    }

    fun onResetOnboardingClicked() {
        viewModelScope.launch {
            appSettingsRepository.resetOnboardingCompleted()
            _uiState.value = OnboardingViewState("Onboarding start")
            navigateUp()
        }
    }
}
