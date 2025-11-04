package nl.q42.template

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.q42.template.core.ui.compose.composables.window.LocalSnackbarHostState
import nl.q42.template.core.ui.compose.composables.window.toSnackBarVisuals
import nl.q42.template.core.ui.presentation.SnackbarManager
import HomeScreen
import OnboardingScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.q42.template.core.navigation.Destination
import nl.q42.template.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview
@Composable
internal fun App() {
    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarChangedEffect(snackbarHostState)

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState
    ) {
        AppTheme {

            val navController = rememberNavController()
            val onNavigate = { destination: Destination ->
                navController.navigate(destination)
            }
            val onBack: () -> Unit = { navController.popBackStack() }

            NavHost(navController = navController, startDestination = Destination.Home) {
                composable<Destination.Home> { HomeScreen(onNavigate, onBack) }
                composable<Destination.Onboarding> { OnboardingScreen(onNavigate, onBack) }
            }
        }
    }
}


/**
 * May set a Snackbar on the [snackbarHostState] if the [SnackbarManager] has a snackbar available.
 * To actually show the snackbar, snackbarHostState has to be used in a Scaffold, such as ScaffoldWithAppBar.
 */
@Composable
private fun SnackbarChangedEffect(snackbarHostState: SnackbarHostState) {
    val snackbarManager: SnackbarManager = koinInject()
    val snackbarSpec by snackbarManager.uiState.collectAsStateWithLifecycle(
        initialValue = null
    )
    val snackbarVisuals = snackbarSpec?.toSnackBarVisuals()

    LaunchedEffect(snackbarVisuals) {
        snackbarHostState.currentSnackbarData?.dismiss()
        if (snackbarVisuals != null) {
            snackbarHostState.showSnackbar(snackbarVisuals)
        }
    }
}
