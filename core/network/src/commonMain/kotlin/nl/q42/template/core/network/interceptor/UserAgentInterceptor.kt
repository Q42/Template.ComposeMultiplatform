package nl.q42.template.core.network.interceptor

import io.ktor.client.plugins.api.createClientPlugin
import nl.q42.template.core.network.di.getPlatformInfo
import nl.q42.template.core.utils.config.AppVersionCode
import nl.q42.template.core.utils.config.AppVersionName

/**
 * Interceptor that adds a User-Agent header with app version and platform information.
 */
val UserAgentInterceptor = createClientPlugin(
    name = "UserAgentInterceptor",
    createConfiguration = ::UserAgentInterceptorConfig
) {
    val appVersionName = pluginConfig.appVersionName
    val appVersionCode = pluginConfig.appVersionCode

    onRequest { request, _ ->
        val platformInfo = getPlatformInfo()
        val userAgentString = "App/${appVersionName.value} (${appVersionCode.value}; $platformInfo)"
        request.headers.append("User-Agent", userAgentString)
    }
}

class UserAgentInterceptorConfig {
    lateinit var appVersionName: AppVersionName
    lateinit var appVersionCode: AppVersionCode
}

