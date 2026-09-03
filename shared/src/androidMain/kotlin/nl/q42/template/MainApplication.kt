package nl.q42.template

import android.app.Application
import android.os.StrictMode
import nl.q42.template.di.createAppModules
import nl.q42.template.di.isDebug
import nl.q42.template.interop.AndroidNativeDependencyExample
import nl.q42.template.logging.AndroidLoggerBootstrap
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AndroidLoggerBootstrap.initialize(context = this, isDebug = isDebug())

        startKoin {
            androidContext(this@MainApplication)
            modules(createAppModules(AndroidNativeDependencyExample()))
        }

        if (isDebug()) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        }
    }
}
