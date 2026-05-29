package nl.q42.template.data.main.di

import nl.q42.template.data.main.AppSettingsRepositoryImpl
import nl.q42.template.data.main.UserRepositoryImpl
import nl.q42.template.data.main.local.UserLocalDataSource
import nl.q42.template.data.main.remote.UserRemoteDataSource
import nl.q42.template.data.main.remote.api.UserApi
import nl.q42.template.domain.main.repo.AppSettingsRepository
import nl.q42.template.domain.main.repo.UserRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val dataPlatformModule: Module

/**
 * Qualifier for ephemeral cache-like preferences.
 *
 * Android: stored under the app cache directory (`Context.cacheDir`), so the OS may clear it.
 * iOS: stored under `NSCachesDirectory`, also treated as disposable cache data.
 */
val qualifierCacheDataStore = named("cache_data_store")

/**
 * Qualifier for persistent app preferences that should survive normal cache cleanup,
 * for example tokens.
 *
 * Android: stored under the no-backup directory (`Context.noBackupFilesDir`), so it is not part of Auto Backup.
 * iOS: stored under `NSApplicationSupportDirectory` with backup excluded.
 *
 * Note: "secure" here describes storage location/lifecycle, not built-in encryption.
 */
val qualifierSecureDataStore = named("secure_data_store")

val mainDataModule = module {

    includes(dataPlatformModule)

    singleOf(::UserRemoteDataSource)

    singleOf(::UserLocalDataSource)

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }

    single<AppSettingsRepository> {
        AppSettingsRepositoryImpl(dataStore = get(qualifierSecureDataStore))
    }

    singleOf(::UserApi)
}

internal const val CACHE_DATA_STORE_FILE_NAME = "app_cache.preferences_pb"
internal const val SECURE_DATA_STORE_FILE_NAME = "app_secure.preferences_pb"
