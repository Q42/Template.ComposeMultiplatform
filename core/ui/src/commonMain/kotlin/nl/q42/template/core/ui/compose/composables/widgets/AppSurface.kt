package nl.q42.template.core.ui.compose.composables.widgets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nl.q42.template.core.ui.theme.AppTheme
import nl.q42.template.core.ui.theme.PreviewAppTheme

@Composable
fun AppSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        color = AppTheme.colors.surface,
        contentColor = AppTheme.colors.textPrimary,
        content = content
    )
}

@Composable
@Preview
private fun AppSurfacePreview() {
    PreviewAppTheme {
        AppSurface(Modifier.fillMaxSize()) {}
    }
}
