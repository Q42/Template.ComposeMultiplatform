package nl.q42.template.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.domain.main.repo.AppSettingsRepository
import org.koin.core.annotation.InjectedParam

class OnboardingViewModel(
    @InjectedParam private val navigator: Navigator,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel(), Navigator by navigator {

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
            navigateBack()
        }
    }

    fun onResetOnboardingClicked() {
        viewModelScope.launch {
            appSettingsRepository.resetOnboardingCompleted()
            _uiState.value = OnboardingViewState("Onboarding start")
            navigateBack()
        }
    }
}
