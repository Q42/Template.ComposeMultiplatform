import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import nl.q42.template.core.navigation.Destination

@Composable
fun OnboardingScreen(
    onNavigate: (Destination) -> Unit,
    onBack: () -> Unit,
) {

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Text("Onboarding Screen")

            Button(onClick = { onBack() }) {
                Text("Go back")
            }
        }
    }
}