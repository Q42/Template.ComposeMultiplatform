package nl.q42.template.core.ui.presentation

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource

/**
 * This class enables you to use string logic in the ViewModel, especially plurals or replacement parameter logic.
 *
 * All strings will be refreshed on view recreation (i.e. after locale or other config changes).
 *
 * There is one edge case: the formatArgs are not refreshed on config changes, so if you use a string with a
 * replacement parameter that is f.e. a localized date string, move your logic to the view instead of using [ViewStateString].
 */
sealed class ViewStateString {
    data class Res(
        val stringRes: StringResource,
        val formatArgs: List<Any> = listOf()
    ) : ViewStateString() {
        // Allow constructing ViewStateString.Res with varargs instead of passing a list
        @Suppress("unused")
        constructor(stringRes: StringResource, vararg formatArgs: Any) : this(stringRes, formatArgs.toList())
    }

    /**
     * Be careful!! Check this https://kotlinlang.org/docs/multiplatform/compose-multiplatform-resources-usage.html#plurals
     *
     * For CMP positional placeholders are required, so if you want to use formatArgs, make sure to use them as well
     * in the string resource and add the position to the placeholder (e.g. %1$s instead of %s).
     */
    data class PluralRes(
        val pluralRes: PluralStringResource,
        val quantity: Int,
        val formatArgs: List<Any> = listOf(quantity)
    ) : ViewStateString() {
        // Allow constructing ViewStateString.PluralRes with varargs instead of passing a list
        @Suppress("unused")
        constructor(pluralRes: PluralStringResource, count: Int, vararg formatArgs: Any) : this(
            pluralRes,
            count,
            formatArgs.toList()
        )
    }

    data class Basic(val value: String) : ViewStateString()
}

fun String.toViewStateString(): ViewStateString.Basic = ViewStateString.Basic(this)
