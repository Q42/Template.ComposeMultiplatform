package nl.q42.template.feature.home.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import nl.q42.template.feature.home.presentation.HomeViewState
import nl.q42.template.core.ui.compose.composables.text.BodyText
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.composables.window.ColumnScreenContent
import nl.q42.template.core.ui.compose.getCompose
import nl.q42.template.core.ui.presentation.toViewStateString
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun HomeContent(
    viewState: HomeViewState,
    insetsPadding: PaddingValues,
    onLoadClicked: () -> Unit,
    onOpenSecondScreenClicked: () -> Unit,
    onOpenOnboardingClicked: () -> Unit,
    onShowDummySnackBarClicked: () -> Unit,
    onShowDialogClicked: () -> Unit,
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
                    Text(text = viewState.userEmailTitle.getCompose())
                    Text(text = viewState.interopExampleText)
                }

                is HomeViewState.Loading -> CircularProgressIndicator()
                is HomeViewState.Error -> BodyText("Error", AppTheme.colors.error)
            }

            Spacer(Modifier.height(Dimens.componentSpacingVertical))

            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dimens.buttonSpacingVertical)
            ) {
                AppButton("Refresh", onClick = onLoadClicked)

                AppButton("Open second screen", onClick = onOpenSecondScreenClicked)

                AppButton("Open Onboarding", onClick = onOpenOnboardingClicked)

                AppButton("Disabled button", enabled = false) {}

                AppButton("Show dummy SnackBar", onClick = onShowDummySnackBarClicked)

                AppButton("Show Dialog for userid 1337", onClick = onShowDialogClicked)
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
            onOpenSecondScreenClicked = {},
            onOpenOnboardingClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = { },
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
            onOpenSecondScreenClicked = {},
            onOpenOnboardingClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = { },
        )
    }
}

@Preview
@Composable
private fun HomeContentEmptyPreview() {
    PreviewAppTheme {
        HomeContent(
            HomeViewState.Content(
                userEmailTitle = "preview@preview.com".toViewStateString(),
                interopExampleText = "interop example text"
            ),
            insetsPadding = PaddingValues(),
            onLoadClicked = {},
            onOpenSecondScreenClicked = {},
            onOpenOnboardingClicked = {},
            onShowDummySnackBarClicked = {},
            onShowDialogClicked = { }
        )
    }
}
