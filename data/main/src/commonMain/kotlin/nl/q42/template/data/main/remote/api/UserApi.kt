package nl.q42.template.data.main.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import nl.q42.template.core.utils.config.ApiBaseUrl

class UserApi(
    private val httpClient: HttpClient,
    private val apiBaseUrl: ApiBaseUrl, // must end with a slash
) {

    suspend fun getUser(): HttpResponse =
        // mock endpoint that returns a user json
        httpClient.get(
            "${apiBaseUrl.value}todos/1"
        )
}