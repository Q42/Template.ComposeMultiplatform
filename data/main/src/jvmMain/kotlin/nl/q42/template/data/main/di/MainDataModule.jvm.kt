package nl.q42.template.data.main.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual val dataPlatformModule: Module = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            File(System.getProperty("user.home"), ".template-app")
                .also { it.mkdirs() }
                .let { File(it, DATA_STORE_FILE_NAME) }
                .absolutePath
                .toPath()
        }
    }

    // Add more JVM-specific dependencies here if needed
}
