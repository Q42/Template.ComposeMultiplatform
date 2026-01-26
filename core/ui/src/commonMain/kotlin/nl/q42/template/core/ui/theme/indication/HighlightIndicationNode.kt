package nl.q42.template.core.ui.theme.indication

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.TimeSource

class HighlightIndicationNodeFactory(
    private val color: Color
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return HighlightIndicationNode(interactionSource, color)
    }

    override fun hashCode(): Int = color.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HighlightIndicationNodeFactory) return false
        return color == other.color
    }
}

private class HighlightIndicationNode(
    private val interactionSource: InteractionSource,
    private val color: Color
) : Modifier.Node(), DrawModifierNode {
    var isPressed = false
    var pressStartTime = TimeSource.Monotonic.markNow()

    private fun animateToPressed() {
        isPressed = true
        pressStartTime = TimeSource.Monotonic.markNow()
        invalidateDraw()
    }

    private suspend fun animateToResting() {
        val elapsedTime = pressStartTime.elapsedNow().inWholeMilliseconds
        val remainingTime = 100L - elapsedTime
        if (remainingTime > 0) {
            delay(remainingTime)
        }
        isPressed = false
        invalidateDraw()
    }

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> animateToPressed()
                    is PressInteraction.Release -> animateToResting()
                    is PressInteraction.Cancel -> animateToResting()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        if (isPressed) {
            drawRect(color = color)
        }
        drawContent()
    }
}