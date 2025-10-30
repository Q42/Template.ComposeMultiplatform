package nl.q42.template

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.q42.template.core.ui.compose.composables.window.LocalSnackbarHostState
import nl.q42.template.core.ui.compose.composables.window.toSnackBarVisuals
import nl.q42.template.core.ui.presentation.SnackbarManager
import nl.q42.template.theme.AppTheme
import nl.q42.template.feature.home.ui.HomeScreen
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
            HomeScreen()
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
