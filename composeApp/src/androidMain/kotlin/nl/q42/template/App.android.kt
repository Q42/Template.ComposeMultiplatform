package nl.q42.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import nl.q42.template.di.createAppModules
import nl.q42.template.interop.AndroidNativeDependencyExample
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@AppActivity.application)
            modules(
                createAppModules(AndroidNativeDependencyExample())
            )
        }

        enableEdgeToEdge()
        setContent { App() }
    }
}
