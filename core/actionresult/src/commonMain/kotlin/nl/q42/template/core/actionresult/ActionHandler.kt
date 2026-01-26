package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult

/**
 * Shortcut to react on success and failure states of an action.
 *
 * @param action The ActionResult to handle
 * @param onSuccess Called with the success data if the action succeeded
 * @param onFailure Called with the error if the action failed
 */
suspend fun <S, E> handleAction(
    action: ActionResult<S, E>,
    onSuccess: suspend (S) -> Unit,
    onFailure: suspend (E) -> Unit,
) {
    when (action) {
        is ActionResult.Success -> onSuccess(action.data)
        is ActionResult.Failure -> onFailure(action.error)
    }
}
