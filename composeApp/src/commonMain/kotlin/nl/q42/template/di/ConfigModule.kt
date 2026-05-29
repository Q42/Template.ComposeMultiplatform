package nl.q42.template.di

import nl.q42.template.BuildKonfig
import nl.q42.template.core.utils.config.ApiBaseUrl
import nl.q42.template.core.utils.config.AppApplicationId
import nl.q42.template.core.utils.config.AppScheme
import nl.q42.template.core.utils.config.AppVersionCode
import nl.q42.template.core.utils.config.AppVersionName
import nl.q42.template.core.utils.config.IsLogHttpCalls
import org.koin.dsl.module

/**
 * All application wide config can go in here. Used so that other modules don't need to access the BuildConfig, which has drawbacks and can cause bugs:
 * https://blog.dipien.com/stop-generating-the-buildconfig-on-your-android-modules-7d82dd7f20f1
 */
val configModule = module {
    single { ApiBaseUrl(BuildKonfig.API_BASE_URL) }
    single { IsLogHttpCalls(isDebug()) }
    single { AppScheme(BuildKonfig.SCHEME) }
    single { AppVersionName(BuildKonfig.APP_VERSION_NAME) }
    single { AppVersionCode(BuildKonfig.APP_VERSION_CODE.toLong()) }
    single { AppApplicationId(getApplicationId()) }
}

expect fun isDebug(): Boolean

expect fun getApplicationId(): String?