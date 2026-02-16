package nl.q42.template.feature.home.ui

import androidx.compose.runtime.Composable
import nl.q42.template.core.navigation.viewmodel.Navigator
import nl.q42.template.core.ui.compose.OnLifecycleResume
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.feature.home.presentation.HomeModalExampleViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun HomeModalExampleScreen(
    navigator: Navigator,
    viewModel: HomeModalExampleViewModel = koinViewModel(
        parameters = {
            parametersOf(navigator)
        }
    )
) {

    OnLifecycleResume(viewModel::onScreenResumed)

    ScaffoldWithAppBar(
        title = "Modal example",
        onNavIconClicked = null, // TODO: should we use a nav icon to close the modal?
        content = { insetsPadding ->
            HomeModalExampleContent(
                insetsPadding = insetsPadding,
                onCloseClicked = viewModel::onCloseClicked,
            )
        },
    )
}
