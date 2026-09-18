package nl.q42.template

import android.app.Application
import android.os.StrictMode
import co.touchlab.kermit.LogcatWriter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import nl.q42.template.di.createAppModules
import nl.q42.template.di.isDebug
import nl.q42.template.interop.AndroidSampleNativeDependency
import nl.q42.template.logging.AndroidCrashReporter
import nl.q42.template.logging.LoggerBootstrap
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(createAppModules(AndroidSampleNativeDependency()))
        }

        if (isDebug()) {
            // Disable Firebase performance monitoring and analytics in debug builds here
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
            // Enable Firebase performance monitoring and analytics in release builds here
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
        }

        LoggerBootstrap.initialize(
            isDebug = isDebug(),
            logWriter = LogcatWriter(),
            crashReporter = AndroidCrashReporter()
        )
    }
}
