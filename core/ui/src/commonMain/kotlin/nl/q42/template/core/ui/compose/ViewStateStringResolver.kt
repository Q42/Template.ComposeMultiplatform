package nl.q42.template.core.ui.compose

import androidx.compose.runtime.Composable
import nl.q42.template.core.ui.presentation.ViewStateString
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Resolve a ViewStateString to a string from a Compose context.
 */
@Composable
fun ViewStateString.get(): String {
    return when (this) {
        is ViewStateString.Res -> {
            // Map any nested ViewStateStrings to their resolved values.
            val resolvedArguments =
                this.formatArgs.map { if (it is ViewStateString) it.get() else it }
                    .toTypedArray()
            stringResource(resource = this.stringRes, formatArgs = resolvedArguments)
        }

        is ViewStateString.PluralRes -> {
            pluralStringResource(resource = this.pluralRes, quantity = quantity, quantity)
        }

        is ViewStateString.Basic -> this.value
    }
}

/**
 * Resolve a ViewStateString to a string from outside of a Compose context.
 */
suspend fun ViewStateString.getLegacy(): String {
    return when (this) {
        is ViewStateString.Res -> {
            // Map any nested ViewStateStrings to their resolved values.
            val resolvedArguments =
                this.formatArgs.map { if (it is ViewStateString) it.getLegacy() else it }
                    .toTypedArray()
            getString(this.stringRes, resolvedArguments)
        }

        is ViewStateString.PluralRes -> {
            getPluralString(resource = pluralRes, quantity = quantity, quantity)
        }

        is ViewStateString.Basic -> this.value
    }
}
