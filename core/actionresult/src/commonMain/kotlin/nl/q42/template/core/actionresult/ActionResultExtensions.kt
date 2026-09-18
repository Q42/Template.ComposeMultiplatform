package nl.q42.template.core.actionresult

import nl.q42.template.core.actionresult.model.ActionResult

/**
 * Returns the success data if this is a Success, or null if this is a Error.
 */
fun <S, E> ActionResult<S, E>.getDataOrNull(): S? = when (this) {
    is ActionResult.Error -> null
    is ActionResult.Success -> data
}

/**
 * Returns the error if this is a Error, or null if this is a Success.
 */
fun <S, E> ActionResult<S, E>.getErrorOrNull(): E? = when (this) {
    is ActionResult.Error -> error
    is ActionResult.Success -> null
}

/**
 * Maps an ActionResult with result type S into an ActionResult with result type T,
 * preserving the error type.
 *
 * Example usage: `map(SampleUserDTO::toSampleUser)`
 */
fun <S, T, E> ActionResult<S, E>.map(mapper: (S) -> T): ActionResult<T, E> = when (this) {
    is ActionResult.Error -> this
    is ActionResult.Success -> ActionResult.Success(mapper(this.data))
}

/**
 * Maps an ActionResult with error type E into an ActionResult with error type F,
 * preserving the success type.
 *
 * Example usage: `result.mapError { error -> error.toUserMessage() }`
 */
fun <S, E, F> ActionResult<S, E>.mapError(mapper: (E) -> F): ActionResult<S, F> = when (this) {
    is ActionResult.Error -> ActionResult.Error(mapper(this.error))
    is ActionResult.Success -> this
}

/**
 * Maps an ActionResult with result type List<S> into an ActionResult with result type List<T>,
 * preserving the error type.
 *
 * Example usage: `mapList(SampleUserDTO::toSampleUser)`
 */
fun <S, T, E> ActionResult<List<S>, E>.mapList(mapper: (S) -> T): ActionResult<List<T>, E> = when (this) {
    is ActionResult.Error -> this
    is ActionResult.Success -> ActionResult.Success(this.data.map { mapper(it) })
}

/**
 * Chains an ActionResult-returning function, flattening the nested result.
 * Useful for sequential operations that may fail.
 *
 * Example usage:
 * ```
 * fetchUser()
 *     .flatMap { user -> validateUser(user) }
 *     .flatMap { validUser -> saveUser(validUser) }
 * ```
 */
fun <S, T, E> ActionResult<S, E>.flatMap(
    transform: (S) -> ActionResult<T, E>
): ActionResult<T, E> = when (this) {
    is ActionResult.Error -> this
    is ActionResult.Success -> transform(this.data)
}

/**
 * Executes a side effect if this is a Success, returns the original result.
 * Useful for logging, analytics, or other side effects without transforming the result.
 *
 * Example usage: `result.onSuccess { user -> logger.info("User loaded: $user") }`
 */
inline fun <S, E> ActionResult<S, E>.onSuccess(
    action: (S) -> Unit
): ActionResult<S, E> {
    if (this is ActionResult.Success) action(data)
    return this
}

/**
 * Executes a side effect if this is a Error, returns the original result.
 * Useful for logging, analytics, or other side effects without transforming the result.
 *
 * Example usage: `result.onError { error -> logger.error("Failed: $error") }`
 */
inline fun <S, E> ActionResult<S, E>.onError(
    action: (E) -> Unit
): ActionResult<S, E> {
    if (this is ActionResult.Error) action(error)
    return this
}

/**
 * Returns the success value or a default value if this is a Error.
 *
 * Example usage: `result.getOrDefault(User.empty)`
 */
fun <S, E> ActionResult<S, E>.getOrDefault(default: S): S = when (this) {
    is ActionResult.Error -> default
    is ActionResult.Success -> data
}

/**
 * Returns the success value or computes a default value from the error if this is a Error.
 *
 * Example usage: `result.getOrElse { error -> User.guest }`
 */
inline fun <S, E> ActionResult<S, E>.getOrElse(default: (E) -> S): S = when (this) {
    is ActionResult.Error -> default(error)
    is ActionResult.Success -> data
}

/**
 * Combines two ActionResults into a Pair if both are successful.
 * Returns the first error encountered.
 *
 * Example usage: `result1.zip(result2)`
 */
fun <S1, S2, E> ActionResult<S1, E>.zip(
    other: ActionResult<S2, E>
): ActionResult<Pair<S1, S2>, E> = when (this) {
    is ActionResult.Error -> this
    is ActionResult.Success -> when (other) {
        is ActionResult.Error -> other
        is ActionResult.Success -> ActionResult.Success(this.data to other.data)
    }
}