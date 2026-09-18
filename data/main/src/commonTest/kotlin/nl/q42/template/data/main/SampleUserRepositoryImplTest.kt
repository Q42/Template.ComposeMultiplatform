package nl.q42.template.data.main

import app.cash.turbine.test
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import nl.q42.template.core.actionresult.model.ActionResult
import nl.q42.template.core.actionresult.model.ApiError
import nl.q42.template.core.utils.config.ApiBaseUrl
import nl.q42.template.data.main.local.SampleUserLocalDataSource
import nl.q42.template.data.main.remote.SampleUserRemoteDataSource
import nl.q42.template.data.main.remote.api.SampleUserApi
import nl.q42.template.domain.main.model.SampleUser
import nl.q42.template.domain.main.model.SampleUserName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SampleUserRepositoryImplTest {

    private fun createRepository(mockEngine: MockEngine): SampleUserRepositoryImpl {
        val httpClient = HttpClient(mockEngine) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        val userApi = SampleUserApi(httpClient, ApiBaseUrl("https://test.com/"))

        return SampleUserRepositoryImpl(
            userRemoteDataSource = SampleUserRemoteDataSource(userApi),
            userLocalDataSource = SampleUserLocalDataSource(),
        )
    }

    @Test
    fun `fetchUser returns Success and stores the fetched user locally`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("""{"title":"Ada"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val repository = createRepository(mockEngine)

        repository.getUserFlow().test {
            val result = repository.fetchUser()

            assertTrue(result is ActionResult.Success)
            assertEquals(SampleUser(SampleUserName("Ada")), awaitItem())
        }
    }

    @Test
    fun `fetchUser returns Error and does not store anything locally when the API call fails`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.NotFound,
                headers = headersOf()
            )
        }
        val repository = createRepository(mockEngine)

        repository.getUserFlow().test {
            val result = repository.fetchUser()

            assertTrue(result is ActionResult.Error)
            assertTrue(result.error is ApiError.NotFoundError)
            expectNoEvents()
        }
    }

    @Test
    fun `getUserFlow reflects the most recently stored user`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel("""{"title":"Ada"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf("Content-Type", "application/json")
            )
        }
        val repository = createRepository(mockEngine)

        repository.getUserFlow().test {
            repository.fetchUser()
            assertEquals(SampleUser(SampleUserName("Ada")), awaitItem())

            repository.fetchUser()
            assertEquals(SampleUser(SampleUserName("Ada")), awaitItem())
        }
    }
}
