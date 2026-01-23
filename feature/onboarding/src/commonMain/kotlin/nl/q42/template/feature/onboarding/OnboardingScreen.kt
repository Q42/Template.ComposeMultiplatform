import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.feature.onboarding.presentation.OnboardingViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OnboardingScreen(
    navigator: Navigator,
    viewModel: OnboardingViewModel = koinViewModel(
        parameters = {
            parametersOf(navigator)
        }
    )
) {

    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Center,
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            val uiState by viewModel.uiState.collectAsState()

            Text("Onboarding Screen")

            Text(uiState.title)

            Button(onClick = { viewModel.onCompleteOnboardingClicked() }) {
                Text("Complete Onboarding")
            }

            Button(onClick = { viewModel.onResetOnboardingClicked() }) {
                Text("Reset Onboarding")
            }
        }
    }
}
