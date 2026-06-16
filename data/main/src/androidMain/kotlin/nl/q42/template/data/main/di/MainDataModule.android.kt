package nl.q42.template.data.main.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toOkioPath
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataPlatformModule: Module = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            get<Context>().noBackupFilesDir.resolve(DATA_STORE_FILE_NAME).toOkioPath()
        }
    }

    // Add more android-specific dependencies here if needed
}