package nl.q42.template.feature.home.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.composables.widgets.NativeButton
import nl.q42.template.core.ui.compose.composables.window.ColumnScreenContent
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme

@Composable
internal fun InteropExamplesContent(
    insetsPadding: PaddingValues,
    onExecuteNativeExampleMethodClicked: () -> Unit,
    onExecuteNativeAsyncExampleMethodClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    ColumnScreenContent(
        modifier = modifier,
        insetsPadding = insetsPadding,
        horizontalAlignment = CenterHorizontally,
        content = {

            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dimens.buttonSpacingVertical)
            ) {
                AppButton("Execute native example method", onClick = onExecuteNativeExampleMethodClicked)

                AppButton("Execute native async example method", onClick = onExecuteNativeAsyncExampleMethodClicked)

                NativeButton(
                    text = "Native Button",
                    onClick = {
                        println("Native button was clicked")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                )

                AppButton("Go back", onClick = onBackClicked)
            }
        }
    )
}

@Preview
@Composable
private fun InteropExamplesContentPreview() {
    PreviewAppTheme {
        InteropExamplesContent(
            insetsPadding = PaddingValues(),
            onExecuteNativeExampleMethodClicked = {},
            onExecuteNativeAsyncExampleMethodClicked = {},
            onBackClicked = {},
        )
    }
}

