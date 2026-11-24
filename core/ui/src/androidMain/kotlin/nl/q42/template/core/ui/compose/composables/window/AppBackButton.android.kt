package nl.q42.template.core.ui.compose.composables.window

import androidx.compose.runtime.Composable

@Composable
actual fun AppBackButton(
    onClick: () -> Unit,
    contentDescription: String,
) = IconBackButton(onClick, contentDescription)
