package nl.q42.template.feature.samplehome.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.composables.widgets.NativeButton
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.action_back
import nl.q42.template.core.ui.resources.sample_interop_execute_native_async_method
import nl.q42.template.core.ui.resources.sample_interop_execute_native_method
import nl.q42.template.core.ui.resources.sample_interop_native_button
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SampleInteropContent(
    insetsPadding: PaddingValues,
    onExecuteNativeMethodClicked: () -> Unit,
    onExecuteNativeAsyncMethodClicked: () -> Unit,
    onNativeButtonClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(Dimens.buttonSpacingVertical, Alignment.CenterVertically),
        modifier = modifier
            .padding(insetsPadding)
            .fillMaxSize()
    ) {
        AppButton(stringResource(Res.string.sample_interop_execute_native_method), onClick = onExecuteNativeMethodClicked)

        AppButton(stringResource(Res.string.sample_interop_execute_native_async_method), onClick = onExecuteNativeAsyncMethodClicked)

        NativeButton(
            text = stringResource(Res.string.sample_interop_native_button),
            onClick = onNativeButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.screenPaddingHorizontal)
                .height(ButtonDefaults.MinHeight)
        )

        AppButton(stringResource(Res.string.action_back), onClick = onBackClicked)
    }
}

@Preview
@Composable
private fun SampleInteropContentPreview() {
    PreviewAppTheme {
        SampleInteropContent(
            insetsPadding = PaddingValues(),
            onExecuteNativeMethodClicked = {},
            onExecuteNativeAsyncMethodClicked = {},
            onNativeButtonClicked = {},
            onBackClicked = {},
        )
    }
}
