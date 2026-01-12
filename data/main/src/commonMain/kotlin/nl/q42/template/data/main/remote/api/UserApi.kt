package nl.q42.template.data.main.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import nl.q42.template.core.utils.config.ApiMainPath

class UserApi(
    private val httpClient: HttpClient,
    private val apiMainPath: ApiMainPath, // must end on a slash
) {

    suspend fun getUser(): HttpResponse = httpClient.get("${apiMainPath.value}user/")
}