package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult

/**
 * Returns the success data if this is a Success, or null if this is a Failure.
 */
fun <S, E> ActionResult<S, E>.getDataOrNull(): S? = when (this) {
    is ActionResult.Failure -> null
    is ActionResult.Success -> data
}

/**
 * Returns the error if this is a Failure, or null if this is a Success.
 */
fun <S, E> ActionResult<S, E>.getErrorOrNull(): E? = when (this) {
    is ActionResult.Failure -> error
    is ActionResult.Success -> null
}

/**
 * Maps an ActionResult with result type S into an ActionResult with result type T,
 * preserving the error type.
 *
 * Example usage: `userEntityActionResult.map(UserEntity::mapToUser)`
 */
fun <S, T, E> ActionResult<S, E>.map(mapper: (S) -> T): ActionResult<T, E> = when (this) {
    is ActionResult.Failure -> this
    is ActionResult.Success -> ActionResult.Success(mapper(this.data))
}

/**
 * Maps an ActionResult with error type E into an ActionResult with error type F,
 * preserving the success type.
 *
 * Example usage: `result.mapError { error -> error.toUserMessage() }`
 */
fun <S, E, F> ActionResult<S, E>.mapError(mapper: (E) -> F): ActionResult<S, F> = when (this) {
    is ActionResult.Failure -> ActionResult.Failure(mapper(this.error))
    is ActionResult.Success -> this
}

/**
 * Maps an ActionResult with result type List<S> into an ActionResult with result type List<T>,
 * preserving the error type.
 *
 * Example usage: `userEntityActionResult.mapList(UserEntity::mapToUser)`
 */
fun <S, T, E> ActionResult<List<S>, E>.mapList(mapper: (S) -> T): ActionResult<List<T>, E> = when (this) {
    is ActionResult.Failure -> this
    is ActionResult.Success -> ActionResult.Success(this.data.map { mapper(it) })
}

/**
 * Folds this ActionResult by applying the onSuccess function if it's a Success,
 * or the onFailure function if it's a Failure.
 *
 * Example usage:
 * ```
 * val message = result.fold(
 *     onSuccess = { data -> "Success: $data" },
 *     onFailure = { error -> "Error: $error" }
 * )
 * ```
 */
inline fun <S, E, R> ActionResult<S, E>.fold(
    onSuccess: (S) -> R,
    onFailure: (E) -> R
): R = when (this) {
    is ActionResult.Success -> onSuccess(data)
    is ActionResult.Failure -> onFailure(error)
}

