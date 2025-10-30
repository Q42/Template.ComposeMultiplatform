package nl.q42.template

import androidx.compose.runtime.*
import nl.q42.template.theme.AppTheme
import nl.q42.template.feature.home.ui.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun App() {
    AppTheme {
        HomeScreen()
    }
}