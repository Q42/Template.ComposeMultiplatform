package nl.q42.template

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import nl.q42.template.core.navigation.Destination
import nl.q42.template.core.ui.compose.composables.window.LocalSnackbarHostState
import nl.q42.template.core.ui.compose.composables.window.toSnackBarVisuals
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.navigation.homeGraph
import nl.q42.template.navigation.onboardingDestinations
import nl.q42.template.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Preview
@Composable
internal fun App() {

    remember {
        Napier.base(DebugAntilog()) // TODO add production logger as well (issues/62)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarChangedEffect(snackbarHostState)

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState
    ) {
        AppTheme {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Destination.HomeGraph
            ) {
                homeGraph(
                    navController = navController,
                )
                onboardingDestinations(navController)
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
