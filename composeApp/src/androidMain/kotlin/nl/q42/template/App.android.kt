package nl.q42.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import nl.q42.template.feature.home.di.homeModule
import org.koin.core.context.startKoin

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            modules(homeModule)
        }
        enableEdgeToEdge()
        setContent { App() }
    }
}
