package nl.q42.template

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.q42.template.core.ui.compose.composables.window.LocalSnackbarHostState
import nl.q42.template.core.ui.compose.composables.window.toSnackBarVisuals
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.navigation.NavigationRoot
import org.koin.compose.koinInject

@Composable
fun App() {

    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarChangedEffect(snackbarHostState)

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState
    ) {
        AppTheme {
            NavigationRoot()
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
