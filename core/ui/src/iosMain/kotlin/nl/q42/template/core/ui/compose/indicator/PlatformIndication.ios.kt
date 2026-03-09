package nl.q42.template.core.ui.compose.indicator

import androidx.compose.foundation.Indication
import androidx.compose.ui.graphics.Color
import nl.q42.template.core.ui.theme.indication.HighlightIndicationNodeFactory

actual fun platformIndication(highlightColor: Color): Indication = HighlightIndicationNodeFactory(color = highlightColor)

