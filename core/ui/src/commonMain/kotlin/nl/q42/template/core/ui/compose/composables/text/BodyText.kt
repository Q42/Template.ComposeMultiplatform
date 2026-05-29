package nl.q42.template.core.ui.compose.composables.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.PreviewAppTheme

@Composable
fun BodyText(text: String, color: Color = AppTheme.colors.textPrimary) {
    Text(
        text = text,
        color = color,
        style = AppTheme.typography.body
    )
}

@Composable
@Preview
private fun BodyTextPreview() {
    PreviewAppTheme {
        BodyText("Body text")
    }
}