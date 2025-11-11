package nl.q42.template.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import nl.q42.template.core.ui.compose.composables.widgets.AppButton
import nl.q42.template.core.ui.compose.composables.window.ColumnScreenContent
import nl.q42.template.core.ui.theme.Dimens
import nl.q42.template.core.ui.theme.PreviewAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeModalExampleContent(
    insetsPadding: PaddingValues,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    ColumnScreenContent(
        modifier = modifier.then(Modifier
            .background(Color.White)
        ),
        insetsPadding = insetsPadding,
        horizontalAlignment = CenterHorizontally,
        content = {

            Text("This is a modal example.")
            Column(
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = spacedBy(Dimens.buttonSpacingVertical)
            ) {
                AppButton("Close modal", onClick = onCloseClicked)
            }
        }
    )
}

@Preview
@Composable
private fun HomeModalExampleContentPreview() {
    PreviewAppTheme {
        HomeModalExampleContent(
            insetsPadding = PaddingValues(),
            onCloseClicked = {},
        )
    }
}
