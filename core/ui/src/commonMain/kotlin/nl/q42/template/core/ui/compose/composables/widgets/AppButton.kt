package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import nl.q42.template.core.ui.compose.indicator.platformIndication
import nl.q42.template.core.ui.theme.AppTheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AppButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = AppTheme.colors.accent,
        contentColor = AppTheme.colors.buttonText,
        disabledContentColor = AppTheme.colors.buttonText.copy(alpha = 0.5f),
        disabledContainerColor = AppTheme.colors.accent.copy(alpha = 0.5f)
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Surface(
        modifier = modifier,
        shape = shape,
        color = if (enabled) colors.containerColor else colors.disabledContainerColor,
        contentColor = contentColor,
    ) {
        ProvideTextStyle(AppTheme.typography.body) {
            Row(
                modifier = Modifier
                    .clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = platformIndication(
                            highlightColor = AppTheme.colors.highlightColor
                        ),
                        onClick = onClick,
                        role = Role.Button
                    )
                    .defaultMinSize(
                        minWidth = ButtonDefaults.MinWidth,
                        minHeight = ButtonDefaults.MinHeight
                    )
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = AppTheme.typography.body,
                    color = contentColor
                )
            }
        }
    }
}

@Composable
@Preview
private fun AppButtonPreview() {
    AppTheme {
        AppButton(
            text = "Button",
            onClick = {}
        )
    }
}
