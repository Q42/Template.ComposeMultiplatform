package nl.q42.template.core.ui.compose.composables.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.compose.get
import nl.q42.template.core.ui.presentation.ViewStateString
import nl.q42.template.core.ui.presentation.dialog.DialogData
import nl.q42.template.core.ui.resources.Res
import nl.q42.template.core.ui.resources.generic_ok
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.PreviewAppTheme
import org.jetbrains.compose.resources.stringResource

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
                    text = it.get(),
                    color = contentColor
                )
            }
        },
        text = {
            Text(
                text = data.description.get(),
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
                        .confirmButtonTitle?.get()
                        ?: stringResource(Res.string.generic_ok),
                    color = contentColor
                )
            }
        },
        dismissButton = data.dismissButtonTitle?.let { dismissButton ->
            {
                TextButton(onClick = { onDismissed(data.tag) }) {
                    Text(
                        text = dismissButton.get(),
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
