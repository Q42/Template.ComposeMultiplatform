package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun NativeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
)