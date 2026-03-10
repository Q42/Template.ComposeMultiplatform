package nl.q42.template.core.network.interceptor

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Interceptor that sets the default Content-Type header to application/json for all requests.
 */
val ContentTypeInterceptor = createClientPlugin(
    name = "ContentTypeInterceptor"
) {
    onRequest { request, _ ->
        request.contentType(ContentType.Application.Json)
    }
}

