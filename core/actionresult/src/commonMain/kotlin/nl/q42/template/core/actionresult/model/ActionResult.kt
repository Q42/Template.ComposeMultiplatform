package nl.q42.template.core.actionresult.model

/**
 * A generic result type that can hold either a success value or an error value.
 * This follows the Railway-Oriented Programming pattern.
 *
 * @param S The type of the success value
 * @param E The type of the error value
 */
sealed class ActionResult<out S, out E> {

    /**
     * Represents a successful result with data.
     */
    data class Success<S>(val data: S) : ActionResult<S, Nothing>()

    /**
     * Represents a failed result with an error.
     */
    data class Error<E>(val error: E) : ActionResult<Nothing, E>()
}

/**
 * Domain-specific error types for API operations.
 * These errors are used throughout the data layer when communicating with remote APIs.
 */
sealed class ApiError {

    data class UnAuthorized(val throwable: Throwable, val message: String?) : ApiError()

    data class TooManyRequests(val throwable: Throwable) : ApiError()

    data class ParseError(
        val throwable: Throwable = Throwable("API error format is invalid"),
        val httpStatusCode: Int? = null
    ) : ApiError()

    data class ServerError(val throwable: Throwable, val message: String) : ApiError()

    data object NotFoundError : ApiError()

    data class NetworkError(val throwable: Throwable) : ApiError()

    data class Other(val throwable: Throwable) : ApiError()
}

/**
 * Type alias for ActionResult with ApiError as the error type.
 * This provides convenience for API-related results.
 *
 * Example usage: `ApiResult<User>` instead of `ActionResult<User, ApiError>`
 */
typealias ApiResult<T> = ActionResult<T, ApiError>

