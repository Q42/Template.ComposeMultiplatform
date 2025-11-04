package nl.q42.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import nl.q42.template.di.appModules
import nl.q42.template.interop.AndroidNativeDependencyExample
import nl.q42.template.interop.NativeDependencyExample
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            modules(
                appModules,
                module {
                    single<NativeDependencyExample> { AndroidNativeDependencyExample() }
                }
            )
        }

        enableEdgeToEdge()
        setContent { App() }
    }
}
