package nl.q42.template.di

import nl.q42.template.core.utils.config.ApiMainPath
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
    // TODO add a multiplatform build config to get these values from (issues/61), hardcoding for now:
    single { ApiMainPath("https://jsonplaceholder.typicode.com/") }
    single { IsLogHttpCalls(true) }
    single { AppScheme("templage") }
    single { AppVersionName("0.1-dev") }
    single { AppVersionCode(1) }
}