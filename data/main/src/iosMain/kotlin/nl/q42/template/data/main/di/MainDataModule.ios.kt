package nl.q42.template.data.main.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSApplicationSupportDirectory

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual val dataPlatformModule: Module = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath {
            IOSFilePathHelper.createPath(
                directoryType = NSApplicationSupportDirectory,
                fileName = DATA_STORE_FILE_NAME,
                excludeFromBackup = false,
                failureDirectoryLabel = "application support",
            )
        }
    }

    // Add more iOS-specific dependencies here if needed
}