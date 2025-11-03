package nl.q42.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import nl.q42.template.interop.AndroidInteropProvider

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeKoin(AndroidInteropProvider())

        enableEdgeToEdge()
        setContent { App() }
    }
}
