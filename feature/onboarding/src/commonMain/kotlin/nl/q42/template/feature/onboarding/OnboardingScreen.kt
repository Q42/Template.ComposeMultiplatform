import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import nl.q42.template.feature.onboarding.presentation.OnboardingViewModel

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel
) {

    Scaffold { paddingValues ->
        Column(
            verticalArrangement = Center,
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            Text("Onboarding Screen")

            Button(onClick = { viewModel.onBackClicked() }) {
                Text("Go back")
            }
        }
    }
}