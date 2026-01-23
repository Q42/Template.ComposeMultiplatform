package nl.q42.template.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.core.ui.compose.OnLifecycleResume
import nl.q42.template.core.ui.compose.composables.dialog.InitDialogPresenter
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.feature.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HomeScreen(
    navigator: Navigator,
    viewModel: HomeViewModel = koinViewModel(
        parameters = {
            parametersOf(navigator)
        }
    )
) {

    OnLifecycleResume(viewModel::onScreenResumed)
    InitDialogPresenter(dialogPresenter = viewModel)

    val viewState by viewModel.uiState.collectAsStateWithLifecycle()

    ScaffoldWithAppBar(
        title = null, // home screen does not have a title
        onNavIconClicked = null, // home screen does not have a navigation icon
        content = { insetsPadding ->
            HomeContent(
                viewState = viewState,
                insetsPadding = insetsPadding,
                onLoadClicked = viewModel::onLoadClicked,
                onOpenOnboardingClicked = viewModel::onOpenOnboardingClicked,
                onOpenInteropExamplesClicked = viewModel::onOpenInteropExamplesScreenClicked,
                onShowDummySnackBarClicked = viewModel::onShowDummySnackBarClicked,
                onShowDialogClicked = viewModel::onShowDialogClicked,
                onShowExampleModalClicked = viewModel::onShowExampleModalClicked,
            )
        },
    )
}
