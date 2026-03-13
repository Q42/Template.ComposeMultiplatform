package nl.q42.template.feature.home.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import nl.q42.template.feature.home.presentation.InteropExamplesViewModel

@Composable
fun InteropExamplesScreen(viewModel: InteropExamplesViewModel) {
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
