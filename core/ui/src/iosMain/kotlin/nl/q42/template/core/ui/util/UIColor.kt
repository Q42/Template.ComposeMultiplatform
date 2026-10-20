package nl.q42.template.core.ui.util

import androidx.compose.ui.graphics.Color
import platform.UIKit.UIColor

/** For passing theme colors to native UIKit views, e.g. `tintColor = AppTheme.colors.accent.toUIColor()`. */
fun Color.toUIColor() = UIColor(
    red = red.toDouble(),
    green = green.toDouble(),
    blue = blue.toDouble(),
    alpha = alpha.toDouble(),
)
