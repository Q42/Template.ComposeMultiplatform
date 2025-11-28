package nl.q42.template.feature.home.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.feature.home.presentation.InteropExamplesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InteropExamplesScreen(
    viewModel: InteropExamplesViewModel,
) {
    Scaffold(
        content = { insetsPadding ->
            InteropExamplesContent(
                insetsPadding = insetsPadding,
                onExecuteNativeExampleMethodClicked = viewModel::onExecuteNativeExampleMethodClicked,
                onExecuteNativeAsyncExampleMethodClicked = viewModel::onExecuteNativeAsyncExampleMethodClicked,
                onBackClicked = viewModel::onBackClicked,
            )
        },
    )
}
