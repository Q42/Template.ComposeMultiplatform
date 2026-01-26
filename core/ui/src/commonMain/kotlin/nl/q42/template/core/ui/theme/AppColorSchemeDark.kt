package nl.q42.template.core.ui.theme

object AppColorSchemeDark : AppColorScheme {
    override val buttonText = White
    override val accent = PurpleGrey80
    override val textPrimary = White
    override val textSecondary = Black
    override val surface = Black
    override val surfaceSecondary = White
    override val surfaceSelected = White
    override val error = Pink80
    override val errorContent = White
    override val iOSHighlightColor = White.copy(alpha = 0.15f)
}
