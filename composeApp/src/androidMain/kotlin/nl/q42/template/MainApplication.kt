package nl.q42.template

import android.app.Application
import android.os.StrictMode
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.AndroidNativeDependencyExample
import nl.q42.template.logging.AndroidCrashReporterDelegate
import nl.q42.template.logging.CrashReporter
import nl.q42.template.logging.CrashlyticsLogWriter
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(createAppModules(AndroidNativeDependencyExample()))
        }

        Logger.setTag("Template")

        CrashReporter.setDelegate(AndroidCrashReporterDelegate())

        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = false

            Logger.setMinSeverity(Severity.Verbose)
            Logger.setLogWriters(LogcatWriter())

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

            Logger.setMinSeverity(Severity.Debug)
            Logger.setLogWriters(
                LogcatWriter(),
                CrashlyticsLogWriter()
            )
        }
    }
}