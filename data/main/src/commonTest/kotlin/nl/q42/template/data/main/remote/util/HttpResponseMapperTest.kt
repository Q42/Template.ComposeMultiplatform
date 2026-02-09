package nl.q42.template.data.main.remote.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import nl.q42.template.core.actionresult.model.ActionResult
import nl.q42.template.core.actionresult.model.ApiError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Serializable
data class TestResponse(val message: String)

class HttpResponseMapperTest {

    private fun createClient(mockEngine: MockEngine) = HttpClient(mockEngine) {

        expectSuccess = true // trow exceptions for non-2xx responses, usually as ClientRequestException

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    @Test
    fun `getActionResult returns Success when API call succeeds`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("""{"message":"success"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Success)
        assertEquals("success", result.data.message)
        client.close()
    }

    @Test
    fun `getActionResult returns ParseError when response body is invalid JSON`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("""invalid json"""),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Error)
        assertTrue(result.error is ApiError.ParseError)
        client.close()
    }

    @Test
    fun `getActionResult returns UnAuthorized when status is 401`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.Unauthorized,
                headers = headersOf()
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Error)
        assertTrue(result.error is ApiError.UnAuthorized)
        client.close()
    }

    @Test
    fun `getActionResult returns NotFoundError when status is 404`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.NotFound,
                headers = headersOf()
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Error)
        assertTrue(result.error is ApiError.NotFoundError)
        client.close()
    }

    @Test
    fun `getActionResult returns TooManyRequests when status is 429`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.TooManyRequests,
                headers = headersOf()
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Error)
        assertTrue(result.error is ApiError.TooManyRequests)
        client.close()
    }

    @Test
    fun `getActionResult returns ServerError when status is 5xx`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf()
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Error)
        assertTrue(result.error is ApiError.ServerError)
        client.close()
    }

    @Test
    fun `getActionResult returns Other error for unhandled 4xx status`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.BadRequest,
                headers = headersOf()
            )
        }
        val client = createClient(mockEngine)

        val result = getActionResult<TestResponse> {
            client.get("https://test.com")
        }

        assertTrue(result is ActionResult.Error)
        assertTrue(result.error is ApiError.Other)
        client.close()
    }
}

