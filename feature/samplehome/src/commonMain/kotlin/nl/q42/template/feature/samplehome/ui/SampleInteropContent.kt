package nl.q42.template.feature.samplehome.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.composables.widgets.NativeButton
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme

@Composable
internal fun SampleInteropContent(
    insetsPadding: PaddingValues,
    onExecuteNativeMethodClicked: () -> Unit,
    onExecuteNativeAsyncMethodClicked: () -> Unit,
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
            AppButton("Execute native example method", onClick = onExecuteNativeMethodClicked)

            AppButton("Execute native async example method", onClick = onExecuteNativeAsyncMethodClicked)

            NativeButton(
                text = "Native Button",
                onClick = {
                    println("Native button was clicked")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(40.dp)
            )

            AppButton("Go back", onClick = onBackClicked )
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
            onBackClicked = {},
        )
    }
}

