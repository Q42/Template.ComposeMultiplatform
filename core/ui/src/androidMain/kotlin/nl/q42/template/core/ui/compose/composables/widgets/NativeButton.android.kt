package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun NativeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier
) = Button(
    onClick = onClick,
    modifier = modifier
) {
    Text(text)
}
