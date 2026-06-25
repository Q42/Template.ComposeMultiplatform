package nl.q42.template.core.ui.theme

import androidx.compose.material3.darkColorScheme

object AppColorSchemeDark : AppColorScheme {
    override val buttonText = OnPrimaryDark
    override val accent = PrimaryDark
    override val textPrimary = OnSurfaceDark
    override val textSecondary = OnSurfaceVariantDark
    override val surface = SurfaceDark
    override val surfaceSecondary = SurfaceContainerHighDark
    override val surfaceSelected = SurfaceContainerHighestDark
    override val error = ErrorDark
    override val errorContent = OnErrorDark
    override val highlightColor = OnSurfaceDark.copy(alpha = 0.15f)
}

/**
 * You can create custom color variables for [AppColorScheme] and use mapping
 * from [AppColorSchemeDark] to [androidx.compose.material3.ColorScheme] instead of using [DarkColorScheme] directly
 */
internal val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = ScrimDark,
    inverseSurface = InverseSurfaceDark,
    inverseOnSurface = InverseOnSurfaceDark,
    inversePrimary = InversePrimaryDark,
    surfaceDim = SurfaceDimDark,
    surfaceBright = SurfaceBrightDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
)