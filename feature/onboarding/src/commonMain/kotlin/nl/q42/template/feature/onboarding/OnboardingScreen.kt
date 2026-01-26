package nl.q42.template.feature.onboarding

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import nl.q42.template.feature.onboarding.presentation.OnboardingViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        OnboardingContent(
            uiState = uiState,
            paddingValues = paddingValues,
            onCompleteOnboardingClicked = viewModel::onCompleteOnboardingClicked,
            onResetOnboardingClicked = viewModel::onResetOnboardingClicked,
        )
    }
}
