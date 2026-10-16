package nl.q42.template.feature.sampleonboarding

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.compose.composables.text.BodyText
import nl.q42.template.core.ui.compose.composables.text.H1Text
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.get
import nl.q42.template.core.ui.compose.plus
import nl.q42.template.core.ui.presentation.ViewStateString
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.sample_action_complete_onboarding
import nl.q42.template.core.ui.resources.sample_action_reset_onboarding
import nl.q42.template.core.ui.resources.sample_onboarding_status_start
import nl.q42.template.core.ui.resources.sample_onboarding_title
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.feature.sampleonboarding.presentation.SampleOnboardingViewState
import org.jetbrains.compose.resources.stringResource

@Composable
fun SampleOnboardingContent(
    uiState: SampleOnboardingViewState,
    insetsPadding: PaddingValues,
    onCompleteOnboardingClicked: () -> Unit,
    onResetOnboardingClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = spacedBy(Dimens.buttonSpacingVertical, CenterVertically),
        horizontalAlignment = CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(Dimens.screenContentPadding + insetsPadding),
    ) {
        H1Text(stringResource(Res.string.sample_onboarding_title))

        BodyText(uiState.status.get())

        AppButton(stringResource(Res.string.sample_action_complete_onboarding), onClick = onCompleteOnboardingClicked)

        AppButton(stringResource(Res.string.sample_action_reset_onboarding), onClick = onResetOnboardingClicked)
    }
}

@Preview
@Composable
private fun SampleOnboardingContentPreview() {
    PreviewAppTheme {
        SampleOnboardingContent(
            uiState = SampleOnboardingViewState(ViewStateString.Res(Res.string.sample_onboarding_status_start)),
            insetsPadding = PaddingValues(),
            onCompleteOnboardingClicked = {},
            onResetOnboardingClicked = {},
        )
    }
}
