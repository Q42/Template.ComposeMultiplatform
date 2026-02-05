package nl.q42.template.core.network.interceptor

import io.ktor.client.plugins.api.createClientPlugin
import nl.q42.template.core.network.model.PlatFormInfo
import nl.q42.template.core.utils.config.AppVersionCode
import nl.q42.template.core.utils.config.AppVersionName

const val HEADER_USER_AGENT = "User-Agent"

/**
 * Interceptor that adds a User-Agent header with app version and platform information.
 */
fun createUserAgentInterceptor(
    platformInfo: PlatFormInfo,
) = createClientPlugin(
    name = "UserAgentInterceptor",
    createConfiguration = ::UserAgentInterceptorConfig
) {
    val appVersionName = pluginConfig.appVersionName
    val appVersionCode = pluginConfig.appVersionCode

    onRequest { request, _ ->
        val userAgentString = "App/${appVersionName.value} (${appVersionCode.value}; $platformInfo)"
        request.headers.append(HEADER_USER_AGENT, userAgentString)
    }
}

class UserAgentInterceptorConfig {
    lateinit var appVersionName: AppVersionName
    lateinit var appVersionCode: AppVersionCode
}

