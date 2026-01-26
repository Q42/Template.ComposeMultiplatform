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
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import nl.q42.template.core.ui.compose.indicator.platformIndication
import nl.q42.template.core.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppButton(
    text: String,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = modifier,
        shape = shape,
        color = if (enabled) colors.containerColor else colors.disabledContainerColor,
        contentColor = if (enabled) colors.contentColor else colors.disabledContentColor,
    ) {
        ProvideTextStyle(AppTheme.typography.body) {
            Row(
                modifier = Modifier
                    .semantics { role = Role.Button }
                    .clickable(
                        enabled = enabled,
                        interactionSource = interactionSource,
                        indication = platformIndication(
                            iOSHighlightColor = AppTheme.colors.iOSHighlightColor
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
                    color = AppTheme.colors.buttonText
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
