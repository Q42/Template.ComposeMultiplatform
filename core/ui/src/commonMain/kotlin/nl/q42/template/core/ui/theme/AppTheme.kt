package nl.q42.template.core.ui.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import nl.q42.template.core.ui.compose.composables.widgets.AppSurface

private val LocalAppTypography = staticCompositionLocalOf { AppTypography() }
private val LocalAppColorScheme = staticCompositionLocalOf<AppColorScheme> {
    // Dummy default, will be replaced for the actual tokens by the Provider
    AppColorSchemeLight
}
private val LocalAppShapes = staticCompositionLocalOf { AppShapes() }
internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: AppTypography = AppTheme.typography,
    colors: AppColorScheme = AppTheme.colors,
    shapes: AppShapes = AppTheme.shapes,
    content: @Composable () -> Unit
) {
    val isDarkState = remember(darkTheme) { mutableStateOf(darkTheme) }
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = typography.toMaterialTypography(),
    ) {
        CompositionLocalProvider(
            LocalAppTypography provides typography,
            LocalAppColorScheme provides if (darkTheme) AppColorSchemeDark else AppColorSchemeLight,
            LocalAppShapes provides shapes,
            /** configures the ripple for material components */
            LocalRippleConfiguration provides AppRippleConfiguration,
            /** needed for non-material components to have a material ripple. eg [Modifier.clickable] */
            LocalIndication provides AppRipple,
            /** merges the platform style with our type, @see [ProvideTextStyle] for more context */
            LocalTextStyle provides LocalTextStyle.current.merge(typography.body),
            LocalContentColor provides colors.textPrimary,
            LocalThemeIsDark provides isDarkState,
            content = content
        )
    }
}

object AppTheme {
    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current
    val colors: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current
    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)

@Composable
fun PreviewAppTheme(content: @Composable () -> Unit) {
    AppTheme {
        AppSurface {
            content()
        }
    }
}
