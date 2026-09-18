package nl.q42.template.feature.sampleonboarding

import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.feature.sampleonboarding.presentation.SampleOnboardingViewState

@Composable
fun SampleOnboardingContent(
    uiState: SampleOnboardingViewState,
    paddingValues: PaddingValues,
    onCompleteOnboardingClicked: () -> Unit,
    onResetOnboardingClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {

        Text("Onboarding Screen")

        Text(uiState.title)

        Button(onClick = { onCompleteOnboardingClicked() }) {
            Text("Complete Onboarding")
        }

        Button(onClick = { onResetOnboardingClicked() }) {
            Text("Reset Onboarding")
        }
    }
}

@Preview
@Composable
private fun OnboardingContentPreview() {
    PreviewAppTheme {
        SampleOnboardingContent(
            uiState = SampleOnboardingViewState(title = "Welcome to the app!"),
            paddingValues = PaddingValues(16.dp),
            onCompleteOnboardingClicked = {},
            onResetOnboardingClicked = {},
        )
    }
}
