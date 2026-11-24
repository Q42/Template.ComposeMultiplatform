package nl.q42.template.feature.sampleonboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.feature.sampleonboarding.presentation.SampleOnboardingViewModel

@Composable
fun SampleOnboardingScreen(viewModel: SampleOnboardingViewModel) {

    val uiState by viewModel.uiState.collectAsState()

    ScaffoldWithAppBar(
        title = null,
        onNavIconClicked = viewModel::onBackClicked,
    ) { insetsPadding ->
        SampleOnboardingContent(
            uiState = uiState,
            insetsPadding = insetsPadding,
            onCompleteOnboardingClicked = viewModel::onCompleteOnboardingClicked,
            onResetOnboardingClicked = viewModel::onResetOnboardingClicked,
        )
    }
}
