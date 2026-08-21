package nl.q42.template.core.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object AppColorSchemeLight : AppColorScheme {
    override val buttonText: Color = OnPrimaryLight
    override val accent: Color = PrimaryLight
    override val textPrimary = OnSurfaceLight
    override val textSecondary = OnSurfaceVariantLight
    override val surface = SurfaceLight
    override val surfaceSecondary = SurfaceContainerHighLight
    override val surfaceSelected = SurfaceContainerHighestLight
    override val error = ErrorLight
    override val errorContent = OnErrorLight
    override val highlightColor = OnSurfaceLight.copy(alpha = 0.2f)
}

/**
 * You can create custom color variables for [AppColorScheme] and use mapping
 * from [AppColorSchemeLight] to [androidx.compose.material3.ColorScheme] instead of using [LightColorScheme] directly
 */
internal val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    scrim = ScrimLight,
    inverseSurface = InverseSurfaceLight,
    inverseOnSurface = InverseOnSurfaceLight,
    inversePrimary = InversePrimaryLight,
    surfaceDim = SurfaceDimLight,
    surfaceBright = SurfaceBrightLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
)
