package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import nl.q42.template.core.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.accent,
            contentColor = AppTheme.colors.buttonText,
            disabledContentColor = AppTheme.colors.buttonText.copy(alpha = 0.5f),
            disabledContainerColor = AppTheme.colors.accent.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = text,
            style = AppTheme.typography.body,
            color = AppTheme.colors.buttonText
        )
    }
}

@Composable
@Preview
private fun AppButtonPreview() {
    AppTheme {
        AppButton("Button",) {}
    }
}
