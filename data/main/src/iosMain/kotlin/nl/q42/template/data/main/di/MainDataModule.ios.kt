package nl.q42.template.data.main.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSCachesDirectory

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual val dataPlatformModule: Module = module {
    single<DataStore<Preferences>>(qualifierCacheDataStore) {
        PreferenceDataStoreFactory.createWithPath {
            // Cache-backed preferences: disposable data in NSCachesDirectory.
            IOSFilePathHelper.createPath(
                directoryType = NSCachesDirectory,
                fileName = CACHE_DATA_STORE_FILE_NAME,
                excludeFromBackup = false,
                failureDirectoryLabel = "caches",
            )
        }
    }

    single<DataStore<Preferences>>(qualifierPersistentDataStore) {
        PreferenceDataStoreFactory.createWithPath {
            // Persistent preferences in Application Support, excluded from backups.
            IOSFilePathHelper.createPath(
                directoryType = NSApplicationSupportDirectory,
                fileName = PERSISTENT_DATA_STORE_FILE_NAME,
                excludeFromBackup = true,
                failureDirectoryLabel = "application support",
            )
        }
    }

    // Add more iOS-specific dependencies here if needed
}