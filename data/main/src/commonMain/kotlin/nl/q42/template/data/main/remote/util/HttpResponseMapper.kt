package nl.q42.template.data.main.remote.util

import io.github.aakira.napier.Napier
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import nl.q42.template.core.actionresult.model.ActionResult

// HTTP Status Codes
private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_NOT_FOUND = 404
private const val HTTP_TOO_MANY_REQUESTS = 429
private const val HTTP_SERVER_ERROR_START = 500
private const val HTTP_SERVER_ERROR_END = 599

/**
 * Maps an HttpResponse to an ActionResult with proper error handling.
 *
 * @param T The type of the response body to deserialize
 * @return ActionResult containing either the success data or an appropriate error
 */
internal suspend inline fun <reified T : Any> HttpResponse.toActionResult(): ActionResult<T> {
    return try {
        when {
            status.isSuccess() -> {
                val responseBody = body<T>()
                ActionResult.Success(responseBody)
            }

            status.value == HTTP_UNAUTHORIZED -> {
                ActionResult.Error.UnAuthorized(
                    throwable = Exception("Unauthorized: ${status.value}"),
                    message = "User is not authorized"
                )
            }

            status.value == HTTP_NOT_FOUND -> {
                ActionResult.Error.NotFoundError
            }

            status.value == HTTP_TOO_MANY_REQUESTS -> {
                ActionResult.Error.TooManyRequests(
                    throwable = Exception("Too many requests")
                )
            }

            status.value in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END -> {
                ActionResult.Error.ServerError(
                    throwable = Exception("Server error: ${status.value}"),
                    message = "Server encountered an error"
                )
            }

            else -> {
                ActionResult.Error.ParseError(
                    throwable = Exception("Unexpected status code: ${status.value} ${status.description}"),
                    httpStatusCode = status.value
                )
            }
        }
    } catch (e: Exception) {
        when (e) {
            is SerializationException -> {
                Napier.e("Serialization error: Unable to parse response body", e)
                ActionResult.Error.ParseError(
                    throwable = Exception("Failed to parse response body", e),
                    httpStatusCode = status.value
                )
            }

            is CancellationException -> {
                Napier.d("Request cancelled", e)
                throw e // we throw it again to properly cancel the coroutine
            }

            is HttpRequestTimeoutException -> {
                Napier.e("Request timeout", e)
                ActionResult.Error.NetworkError(throwable = Exception("Request timed out", e))
            }

            is SocketTimeoutException -> {
                Napier.e("Socket timeout", e)
                ActionResult.Error.NetworkError(throwable = Exception("Connection timed out", e))
            }

            is ConnectTimeoutException -> {
                Napier.e("Connection timeout", e)
                ActionResult.Error.NetworkError(throwable = Exception("Connection timed out", e))
            }

            else -> {
                Napier.e("Error processing HTTP response", e)
                ActionResult.Error.Other(e)
            }
        }
    }
}

