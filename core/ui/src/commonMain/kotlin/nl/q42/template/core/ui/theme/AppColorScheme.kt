package nl.q42.template.core.ui.theme

import androidx.compose.ui.graphics.Color

interface AppColorScheme {
    val buttonText: Color
    val accent: Color
    val textPrimary: Color
    val textSecondary: Color
    val surface: Color
    val surfaceSelected: Color
    val surfaceSecondary: Color
    val error: Color
    val errorContent: Color
    val highlightColor: Color
    /** Dims the screen underneath during an iOS push/pop transition. */
    val navigationTransitionDim: Color
}
