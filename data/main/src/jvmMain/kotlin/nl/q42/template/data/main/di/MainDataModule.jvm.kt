package nl.q42.template.data.main.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val dataPlatformModule: Module = module {
    single<DataStore<Preferences>>(qualifierCacheDataStore) {
        PreferenceDataStoreFactory.createWithPath {
            // Cache-backed preferences: stored in the OS temp directory.
            File(System.getProperty("java.io.tmpdir"), CACHE_DATA_STORE_FILE_NAME)
                .absolutePath
                .toPath()
        }
    }

    single<DataStore<Preferences>>(qualifierSecureDataStore) {
        PreferenceDataStoreFactory.createWithPath {
            // Persistent preferences: stored under the user home directory.
            File(System.getProperty("user.home"), SECURE_DATA_STORE_FILE_NAME)
                .absolutePath
                .toPath()
        }
    }

    // Add more JVM-specific dependencies here if needed
}
