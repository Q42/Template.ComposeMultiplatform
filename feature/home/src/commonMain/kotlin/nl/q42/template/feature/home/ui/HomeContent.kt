package nl.q42.template.feature.home.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import nl.q42.template.core.ui.compose.composables.text.BodyText
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.composables.window.ColumnScreenContent
import nl.q42.template.core.ui.compose.get
import nl.q42.template.core.ui.presentation.toViewStateString
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.feature.home.presentation.HomeViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    viewState: HomeViewState,
    insetsPadding: PaddingValues,
    onLoadClicked: () -> Unit,
    onOpenOnboardingClicked: () -> Unit,
    onOpenInteropExamplesClicked: () -> Unit,
    onShowDummySnackBarClicked: () -> Unit,
    onShowDialogClicked: () -> Unit,
    onLogToDatadogClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    ColumnScreenContent(
        modifier = modifier,
        insetsPadding = insetsPadding,
        horizontalAlignment = CenterHorizontally,
        content = {
            when (viewState) {
                is HomeViewState.Content -> {
                    /**
                     * This is dummy. Use the strings file IRL.
                     */
                    BodyText(text = viewState.userEmailTitle.get())
                }

                is HomeViewState.Loading -> CircularProgressIndicator()
                is HomeViewState.Error -> BodyText("Error", AppTheme.colors.error)
            }

            Spacer(Modifier.height(Dimens.componentSpacingVertical))

            val coroutineScope = rememberCoroutineScope()
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var showSheet by rememberSaveable { mutableStateOf(false) }
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dimens.buttonSpacingVertical)
            ) {
                // TODO: Move some example buttons to another screen to keep home screen clean
                AppButton("Refresh", onClick = onLoadClicked)

                AppButton("Open Onboarding", onClick = onOpenOnboardingClicked)

                AppButton("Open interop examples", onClick = onOpenInteropExamplesClicked)

                AppButton("Disabled button", enabled = false) {}

                AppButton("Show dummy SnackBar", onClick = onShowDummySnackBarClicked)

                AppButton("Show Dialog for userid 1337", onClick = onShowDialogClicked)

                /* This opens an actual bottom sheet. It has better transitions, but you cannot use
                viewModel navigation inside it.
                 */
                AppButton("Show bottom sheet", onClick = { showSheet = true })

                AppButton("Log to Datadog", onClick = onLogToDatadogClicked)
            }

            if (showSheet) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { showSheet = false },
                ) {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = spacedBy(Dimens.buttonSpacingVertical, Alignment.CenterVertically),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = Dimens.screenPaddingVertical)
                    ) {
                        Text("This is a modal sheet example.")
                        AppButton(
                            "Close modal",
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    showSheet = false
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun HomeContentErrorPreview() {
    PreviewAppTheme {
        HomeContent(
            viewState = HomeViewState.Error,
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenOnboardingClicked = {},
            onOpenInteropExamplesClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = {},
        )
    }
}

@Preview
@Composable
private fun HomeContentLoadingPreview() {
    PreviewAppTheme {
        HomeContent(
            HomeViewState.Loading,
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenOnboardingClicked = {},
            onOpenInteropExamplesClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = {},
        )
    }
}

@Preview
@Composable
private fun HomeContentEmptyPreview() {
    PreviewAppTheme {
        HomeContent(
            HomeViewState.Content(
                userEmailTitle = "preview@preview.com".toViewStateString()
            ),
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenOnboardingClicked = {},
            onOpenInteropExamplesClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = {},
        )
    }
}
