package nl.q42.template.core.ui.compose.composables.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import nl.q42.template.core.ui.compose.getCompose
import nl.q42.template.core.ui.presentation.dialog.DialogData
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.PreviewAppTheme
import nl.q42.template.core.ui.presentation.ViewStateString
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.generic_ok
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Dialog(
    data: DialogData,
    onDismissed: (Any) -> Unit,
    onConfirmed: (Any) -> Unit,
) {

    val contentColor: Color = AppTheme.colors.textPrimary

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismissed(data.tag)
        },
        title = data.title?.let {
            {
                Text(
                    text = it.getCompose(),
                    color = contentColor
                )
            }
        },
        text = {
            Text(
                text = data.description.getCompose(),
                color = contentColor
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmed(data.tag)
                }
            ) {
                Text(
                    text = data
                        .confirmButtonTitle?.getCompose()
                        ?: stringResource(Res.string.generic_ok),
                    color = contentColor
                )
            }
        },
        dismissButton = data.dismissButtonTitle?.let { dismissButton ->
            {
                TextButton(onClick = { onDismissed(data.tag) }) {
                    Text(
                        text = dismissButton.getCompose(),
                        color = contentColor
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun DialogPreview() {
    PreviewAppTheme {
        Dialog(
            data = DialogData(
                title = ViewStateString.Basic("Title"),
                description = ViewStateString.Basic("Multiline description\nwith new line"),
                confirmButtonTitle = ViewStateString.Basic("OK"),
                dismissButtonTitle = ViewStateString.Basic("Cancel")
            ),
            onDismissed = {},
            onConfirmed = {}
        )
    }
}
