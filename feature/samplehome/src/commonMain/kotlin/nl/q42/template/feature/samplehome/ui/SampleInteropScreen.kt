package nl.q42.template.feature.samplehome.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import nl.q42.template.core.ui.compose.composables.navigation.AppNavigationBarSpacer
import nl.q42.template.feature.samplehome.presentation.SampleInteropViewModel

@Composable
fun SampleInteropScreen(viewModel: SampleInteropViewModel) {
    Scaffold(
        bottomBar = { AppNavigationBarSpacer() },
        content = { insetsPadding ->
            SampleInteropContent(
                insetsPadding = insetsPadding,
                onExecuteNativeMethodClicked = viewModel::onExecuteNativeMethodClicked,
                onExecuteNativeAsyncMethodClicked = viewModel::onExecuteNativeAsyncMethodClicked,
                onNativeButtonClicked = viewModel::onNativeButtonClicked,
                onBackClicked = viewModel::onBackClicked,
            )
        },
    )
}
