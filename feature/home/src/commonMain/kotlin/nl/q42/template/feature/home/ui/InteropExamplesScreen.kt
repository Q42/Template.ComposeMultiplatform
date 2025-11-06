package nl.q42.template.feature.home.ui

import androidx.compose.runtime.Composable
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.feature.home.presentation.InteropExamplesViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InteropExamplesScreen(
    viewModel: InteropExamplesViewModel = koinViewModel ()
) {
    ScaffoldWithAppBar(
        title = null, // home screen does not have a title
        onNavIconClicked = null, // home screen does not have a navigation icon
        content = { insetsPadding ->
            InteropExamplesContent(
                insetsPadding = insetsPadding,
                onExecuteNativeExampleMethodClicked = viewModel::onExecuteNativeExampleMethodClicked,
                onExecuteNativeAsyncExampleMethodClicked = viewModel::onExecuteNativeAsyncExampleMethodClicked,
            )
        },
    )
}
