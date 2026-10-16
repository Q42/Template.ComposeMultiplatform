package nl.q42.template.feature.samplehome.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.action_close
import nl.q42.template.core.ui.resources.error_generic
import nl.q42.template.core.ui.resources.sample_action_disabled_button
import nl.q42.template.core.ui.resources.sample_action_log_to_firebase
import nl.q42.template.core.ui.resources.sample_action_open_interop_examples
import nl.q42.template.core.ui.resources.sample_action_open_onboarding
import nl.q42.template.core.ui.resources.sample_action_refresh
import nl.q42.template.core.ui.resources.sample_action_show_bottom_sheet
import nl.q42.template.core.ui.resources.sample_action_show_dialog
import nl.q42.template.core.ui.resources.sample_action_show_snackbar
import nl.q42.template.core.ui.resources.sample_bottom_sheet_message
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.feature.samplehome.presentation.SampleHomeViewState
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SampleHomeContent(
    viewState: SampleHomeViewState,
    insetsPadding: PaddingValues,
    onLoadClicked: () -> Unit,
    onOpenOnboardingClicked: () -> Unit,
    onOpenSampleInteropClicked: () -> Unit,
    onShowDummySnackBarClicked: () -> Unit,
    onShowDialogClicked: () -> Unit,
    onLogToFirebaseClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    ColumnScreenContent(
        modifier = modifier,
        insetsPadding = insetsPadding,
        horizontalAlignment = CenterHorizontally,
        content = {
            when (viewState) {
                is SampleHomeViewState.Content -> BodyText(text = viewState.userEmailTitle.get())

                is SampleHomeViewState.Loading -> CircularProgressIndicator()
                is SampleHomeViewState.Error -> BodyText(stringResource(Res.string.error_generic), AppTheme.colors.error)
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
                AppButton(stringResource(Res.string.sample_action_refresh), onClick = onLoadClicked)

                AppButton(stringResource(Res.string.sample_action_open_onboarding), onClick = onOpenOnboardingClicked)

                AppButton(stringResource(Res.string.sample_action_open_interop_examples), onClick = onOpenSampleInteropClicked)

                AppButton(stringResource(Res.string.sample_action_disabled_button), enabled = false) {}

                AppButton(stringResource(Res.string.sample_action_show_snackbar), onClick = onShowDummySnackBarClicked)

                AppButton(stringResource(Res.string.sample_action_show_dialog), onClick = onShowDialogClicked)

                /* This opens an actual bottom sheet. It has better transitions, but you cannot use
                viewModel navigation inside it.
                 */
                AppButton(stringResource(Res.string.sample_action_show_bottom_sheet), onClick = { showSheet = true })

                AppButton(stringResource(Res.string.sample_action_log_to_firebase), onClick = onLogToFirebaseClicked)
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
                        BodyText(stringResource(Res.string.sample_bottom_sheet_message))
                        AppButton(
                            stringResource(Res.string.action_close),
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
        SampleHomeContent(
            viewState = SampleHomeViewState.Error,
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenOnboardingClicked = {},
            onOpenSampleInteropClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = {},
        )
    }
}

@Preview
@Composable
private fun HomeContentLoadingPreview() {
    PreviewAppTheme {
        SampleHomeContent(
            SampleHomeViewState.Loading,
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenOnboardingClicked = {},
            onOpenSampleInteropClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = {},
        )
    }
}

@Preview
@Composable
private fun HomeContentEmptyPreview() {
    PreviewAppTheme {
        SampleHomeContent(
            SampleHomeViewState.Content(
                userEmailTitle = "preview@preview.com".toViewStateString()
            ),
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenOnboardingClicked = {},
            onOpenSampleInteropClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = {},
        )
    }
}
