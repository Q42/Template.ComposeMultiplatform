package nl.q42.template.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nl.q42.template.core.ui.compose.OnLifecycleResume
import nl.q42.template.core.ui.compose.composables.dialog.InitDialogPresenter
import nl.q42.template.core.ui.compose.composables.window.ScaffoldWithAppBar
import nl.q42.template.feature.home.presentation.HomeModalExampleViewModel
import nl.q42.template.feature.home.presentation.HomeViewModel

@Composable
fun HomeModalExampleScreen(
    viewModel: HomeModalExampleViewModel
) {

    OnLifecycleResume(viewModel::onScreenResumed)

    ScaffoldWithAppBar(
        title = null, // home screen does not have a title
        onNavIconClicked = null, // home screen does not have a navigation icon
        content = { insetsPadding ->
            HomeModalExampleContent(
                insetsPadding = insetsPadding,
                onCloseClicked = viewModel::onCloseClicked,
            )
        },
    )
}
