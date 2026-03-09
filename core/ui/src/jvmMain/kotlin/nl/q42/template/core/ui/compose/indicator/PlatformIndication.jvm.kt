package nl.q42.template.core.ui.compose.indicator

import androidx.compose.foundation.Indication
import androidx.compose.material3.ripple
import androidx.compose.ui.graphics.Color

actual fun platformIndication(highlightColor: Color): Indication = ripple()

