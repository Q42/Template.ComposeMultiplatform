package nl.q42.template.data.main.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataPlatformModule: Module = module {
    single<DataStore<Preferences>>(qualifierCacheDataStore) {
        PreferenceDataStoreFactory.createWithPath {
            // Cache-backed preferences: disposable and safe to evict.
            get<Context>().cacheDir.resolve(CACHE_DATA_STORE_FILE_NAME).toOkioPath()
        }
    }

    single<DataStore<Preferences>>(qualifierSecureDataStore) {
        PreferenceDataStoreFactory.createWithPath {
            // Persistent preferences outside regular backup flow (for example tokens).
            get<Context>().noBackupFilesDir.resolve(SECURE_DATA_STORE_FILE_NAME).toOkioPath()
        }
    }

    // Add more android-specific dependencies here if needed
}