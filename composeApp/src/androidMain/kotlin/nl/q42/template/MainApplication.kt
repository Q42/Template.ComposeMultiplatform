package nl.q42.template

import android.app.Application
import android.os.StrictMode
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.AndroidNativeDependencyExample
import nl.q42.template.logging.AndroidCrashReporter
import nl.q42.template.logging.LoggerBootstrap
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(createAppModules(AndroidNativeDependencyExample()))
        }

        if (BuildKonfig.DEBUG) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = false
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        } else {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        }

        LoggerBootstrap.initialize(
            logWriter = LogcatWriter(),
            crashReporter = AndroidCrashReporter()
        )
    }
}