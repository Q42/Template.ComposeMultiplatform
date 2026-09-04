package nl.q42.template.feature.sampleonboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.domain.main.repo.SampleAppSettingsRepository

class SampleOnboardingViewModel(
    private val navigator: Navigator,
    private val appSettingsRepository: SampleAppSettingsRepository,
) : ViewModel(), Navigator by navigator {

    private val _uiState = MutableStateFlow(SampleOnboardingViewState("Onboarding start"))
    val uiState: StateFlow<SampleOnboardingViewState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (appSettingsRepository.isOnboardingCompleted()) {
                _uiState.value = SampleOnboardingViewState("Onboarding already completed")
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
            _uiState.value = SampleOnboardingViewState("Onboarding start")
            navigateBack()
        }
    }
}
