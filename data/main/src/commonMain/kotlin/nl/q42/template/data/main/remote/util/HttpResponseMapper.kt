package nl.q42.template.data.main.remote.util

import co.touchlab.kermit.Logger
import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import nl.q42.template.core.actionresult.model.ActionResult
import nl.q42.template.core.actionresult.model.ApiError
import nl.q42.template.core.actionresult.model.ApiResult

// HTTP Status Codes
private const val HTTP_UNAUTHORIZED = 401
private const val HTTP_NOT_FOUND = 404
private const val HTTP_TOO_MANY_REQUESTS = 429

/**
 * Wraps a suspend API call that returns HttpResponse in a try-catch block,
 * executes the call, and maps the result to an ApiResult with proper error handling.
 *
 * @param T The type of the response body to deserialize
 * @param apiCall The suspend function that makes the API call
 * @return ApiResult containing either the success data or an ApiError
 */
internal suspend inline fun <reified T : Any> getActionResult(
    apiCall: suspend () -> HttpResponse
): ApiResult<T> {
    return try {
        val response = apiCall()

        // If we get here, the response was successful (2xx)
        try {
            val responseBody = response.body<T>()
            ActionResult.Success(responseBody)
        } catch (e: SerializationException) {
            Logger.e("Serialization error: Unable to parse response body", e)
            ActionResult.Error(
                ApiError.ParseError(
                    throwable = Exception("Failed to parse response body", e),
                    httpStatusCode = response.status.value
                )
            )
        }
    } catch (e: CancellationException) {
        Logger.d("Request cancelled", e)
        throw e // Re-throw to properly cancel the coroutine
    } catch (e: ClientRequestException) {
        // 4xx errors
        Logger.e("Client request error: ${e.response.status.value}", e)
        when (e.response.status.value) {
            HTTP_UNAUTHORIZED -> {
                ActionResult.Error(
                    ApiError.UnAuthorized(
                        throwable = e,
                        message = "User is not authorized"
                    )
                )
            }

            HTTP_NOT_FOUND -> {
                ActionResult.Error(ApiError.NotFoundError)
            }

            HTTP_TOO_MANY_REQUESTS -> {
                ActionResult.Error(ApiError.TooManyRequests(throwable = e))
            }

            else -> {
                ActionResult.Error(
                    ApiError.Other(
                        throwable = Exception("Client error: ${e.response.status.value} ${e.response.status.description}", e)
                    )
                )
            }
        }
    } catch (e: ServerResponseException) {
        // 5xx errors
        Logger.e("Server error: ${e.response.status.value}", e)
        ActionResult.Error(
            ApiError.ServerError(
                throwable = e,
                message = "Server encountered an error: ${e.response.status.value}"
            )
        )
    } catch (e: RedirectResponseException) {
        // 3xx errors (shouldn't normally happen as Ktor follows redirects by default)
        Logger.e("Redirect error: ${e.response.status.value}", e)
        ActionResult.Error(ApiError.Other(e))
    } catch (e: HttpRequestTimeoutException) {
        Logger.e("Request timeout", e)
        ActionResult.Error(ApiError.NetworkError(throwable = Exception("Request timed out", e)))
    } catch (e: SocketTimeoutException) {
        Logger.e("Socket timeout", e)
        ActionResult.Error(ApiError.NetworkError(throwable = Exception("Connection timed out", e)))
    } catch (e: ConnectTimeoutException) {
        Logger.e("Connection timeout", e)
        ActionResult.Error(ApiError.NetworkError(throwable = Exception("Connection timed out", e)))
    } catch (e: Exception) {
        Logger.e("Error making API call or processing response", e)
        ActionResult.Error(ApiError.Other(e))
    }
}


